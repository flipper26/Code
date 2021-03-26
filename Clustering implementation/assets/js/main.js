$(document).ready(function(){
	// var chNumArray = [];
	// for (i=0; i<1000; i++){
	// 	var CHArray = ok();
	// 	chNumArray.push(CHArray.length);
	// }
	// console.log(chNumArray);


	//on click event for the ok button
	$('#OK_btn').click(function(event) {
		ok();

	});
	//on click event for the refressh button
	$('#Refresh_btn').click(function(event) {
		location.reload();
	});

	//Main  Function
	function ok(){

		//Declarations;
		var h = $('#height_input').val();
		console.log("height: " + h);
		h = toMeters(h);

		var w = $('#width_input').val();
		console.log("height: " + w);
		w = toMeters(w);

		var sensorNum = $('#sensorNum_input').val();
		console.log("sensor Number: " + sensorNum);


		var minDistance = $('#distance_input').val();
		console.log("minDistance: " + minDistance);
		minDistance = toMeters(minDistance);

		var range = $('#range_input').val();
		console.log("range: " + range);
		range = toMeters(range);

		
		var firstValue;
		var secondValue;
		var sensorObject;
		var sensorArray = [];
		var pchArray = [];
		var array_of_PCH_objects_degree = [];
		var array_of_PCH_objects_centrality = [];
		var array_of_PCH_objects_centrality_oneHop_Chance = [];
		array_of_PCH_objects_centrality_OneHop = [];
		var array_of_CH_objects =[];
		var counter = 0;
		var isValid;
		
		var nodeDistance;

		//Drawing the field 
		var c = $("#myCanvas")[0];
		var ctx = c.getContext("2d");
		ctx.clearRect(0, 0, c.width, c.height);
		ctx.canvas.height = h+1;
        ctx.canvas.width = w+1;
		ctx.beginPath();
		ctx.rect(0, 0, w, h);
		ctx.stroke();

		//Draw the base station in the center
		drawfill(w/2, h/2, 7);
		drawText("Base Station", (w/2)-30, (h/2)+20);


		while (counter < sensorNum){

			isValid = true;
			firstValue = randBetween(1, w-1)
			secondValue = randBetween(1, h-1);

			if (sensorArray.length){

				sensorArray.some(function(item){
					nodeDistance = distanceBetweenNodes(firstValue, secondValue, item['x'], item['y']);
					
					if ((nodeDistance < minDistance) && isValid){
						isValid	= false;
						console.log("node Distance: " + nodeDistance + " min Distance: " + minDistance);
						return true;
					}

				});	

				if(isValid){

					sensorObject = {x:firstValue, y:secondValue};
					drawfill(firstValue, secondValue,1); 
					sensorArray.push(sensorObject);
					counter++;
					console.log("counter: " + counter);
				}

			}else{

					sensorObject = {x:firstValue, y:secondValue};
					drawfill(firstValue, secondValue,1); 
					sensorArray.push(sensorObject);
					counter++;
					console.log("counter: " + counter);
			}
		}
		pchArray = PCH(sensorArray, range, w/2, h/2);
		console.log("pchArray: " + pchArray.length);

		array_of_PCH_objects_degree = twoHopnodeDegree(pchArray, sensorArray, range, sensorNum);
		array_of_PCH_objects_centrality = nodeCentrality(array_of_PCH_objects_degree, range);
		array_of_PCH_objects_centrality_OneHop = inOneHopRangeOfAnotherPCH(array_of_PCH_objects_centrality, range, sensorNum);
		array_of_PCH_objects_centrality_oneHop_Chance = computeChance(array_of_PCH_objects_centrality_OneHop);
		array_of_CH_objects = cluster(array_of_PCH_objects_centrality_OneHop, range);


		//Display the array of PCH and their info
		array_of_PCH_objects_centrality_oneHop_Chance.forEach(function(pchObjects){
				$('<div class = "resultLine"></div>').html(pchObjects['name'] + ', x: ' + pchObjects['x'] + ', y: ' + pchObjects['y'] + ', No of nodes: ' + pchObjects['numOfNodesInRange'] 
					 														  + ', 2-hop node degree: ' + pchObjects['NodeDegree'] + ', Node Centrality: ' + pchObjects['Centrality'] 
					 														  + ', Intersection node degree: ' + pchObjects['OneHopNumberOfAllAnotherPCH'] + ', Chance: ' + pchObjects['DispChance']).appendTo('#resluts');		
				console.log(pchObjects);
			});	

		//Display the array of CH and their info
		array_of_CH_objects.forEach(function(chObjects){
				$('<div class = "resultLine"></div>').html(chObjects['name'] + ', x: ' + chObjects['x'] + ', y: ' + chObjects['y'] + ', No of nodes: ' + chObjects['numOfNodesInRange']).appendTo('#resluts');		
				console.log(chObjects);
			});	
	
		return array_of_CH_objects;

	}
//----------------------------------------------functions for clustering--------------------------------------------------------
	
	//Reteurs a potential cluster head array
	function PCH (sensorArray, range, baseX, baseY){
		 var a = 12;
		 var pchArray = [];

		sensorArray.forEach(function(item){
			nodeDistance = distanceBetweenNodes(baseX, baseY, item['x'], item['y']);

			//adds all nodes at a distance between range R+15 and R-15m
			if ((nodeDistance <= 1.5*range+a && nodeDistance > 1.5*range-a) || (nodeDistance <= 4*range+a && nodeDistance > 4*range-a) || (nodeDistance <= 6*range+a && nodeDistance > 6*range-a) || (nodeDistance <= 8*range+a && nodeDistance > 8*range-a)) {
				drawStroke(item['x'],item['y'],3);

				pchArray.push(item);

			}
		});	
		return pchArray;
	}


	//calculate the 2-hop Node degree
	function twoHopnodeDegree(pchArray, sensorArray, range, noOfNodes){
		var count = 1;
		var array_of_nodes_in_range_per_PCH = [];
		var PCH_object; 
		var array_of_PCH_objects = [];

		pchArray.forEach(function(pchItem){

			sensorArray.forEach(function(sensorItem){
				nodeDistance = distanceBetweenNodes(pchItem['x'], pchItem['y'], sensorItem['x'], sensorItem['y']);
				if (nodeDistance < 2*range && nodeDistance != 0){
					array_of_nodes_in_range_per_PCH.push(sensorItem);	
				}
			});	

			PCH_object = {name:"PCH " + count, x: pchItem['x'], y: pchItem['y'], ArrayOfNodesInRange: array_of_nodes_in_range_per_PCH, numOfNodesInRange:array_of_nodes_in_range_per_PCH.length, NodeDegree: ((array_of_nodes_in_range_per_PCH.length)/noOfNodes).toFixed(3)};
			array_of_PCH_objects.push(PCH_object);
			array_of_nodes_in_range_per_PCH = [];
			count++;
		});	

		return array_of_PCH_objects;
	}

	//calculate Node Centrality
	function nodeCentrality(array_of_PCH_objects, range){
		var array_of_nodes_in_range_per_PCH = [];
		var SumOfSquareDistances = 0;
		var centrality = 0;

		array_of_PCH_objects.forEach(function(pchObject){

				array_of_nodes_in_range_per_PCH = pchObject['ArrayOfNodesInRange'];
				array_of_nodes_in_range_per_PCH.forEach(function(sensorInRange){

					nodeDistance = distanceBetweenNodes(pchObject['x'], pchObject['y'], sensorInRange['x'], sensorInRange['y']);
					SumOfSquareDistances = SumOfSquareDistances + Math.pow(nodeDistance, 2);
				});	

				centrality = (Math.sqrt(SumOfSquareDistances/pchObject['numOfNodesInRange']))/((Math.PI)*(Math.pow(2*range, 2)));
				pchObject.Centrality = (centrality).toFixed(5);
				SumOfSquareDistances = 0;
				centrality = 0;
		});	

		return array_of_PCH_objects;

	}

	function inOneHopRangeOfAnotherPCH(array_of_PCH_objects, range, NumberOfNodes){

		var oneHopRangeNodes;
		var nodesInTwoHopRange;
		var array_of_nodes_in_One_hop = [];
		var array_of_nodes_in_One_hop_Of_another_PCH = [];
		var nodeDistance;
		var nodeDistance2
		var sumOfAllOneHopInallNodes = 0;


		array_of_PCH_objects.forEach(function(selectedPchobject){

			array_of_PCH_objects.forEach(function(oneHopPchObject){
				nodesInTwoHopRange = oneHopPchObject['ArrayOfNodesInRange']

				nodesInTwoHopRange.forEach(function(oneHopPchItem){
					nodeDistance = distanceBetweenNodes(oneHopPchObject['x'], oneHopPchObject['y'], oneHopPchItem['x'], oneHopPchItem['y']);
					if (nodeDistance < range && nodeDistance != 0){
						array_of_nodes_in_One_hop.push(oneHopPchItem);	
					}
				});

				array_of_nodes_in_One_hop.forEach(function(item){
					nodeDistance2 = distanceBetweenNodes(selectedPchobject['x'], selectedPchobject['y'], item['x'], item['y']);
					if (nodeDistance2 < 2*range && nodeDistance2 != 0){
						array_of_nodes_in_One_hop_Of_another_PCH.push(item);	
					}
				});
				array_of_nodes_in_One_hop = [];
				sumOfAllOneHopInallNodes = sumOfAllOneHopInallNodes + array_of_nodes_in_One_hop_Of_another_PCH.length;
				array_of_nodes_in_One_hop_Of_another_PCH = [];
				
			});	
			selectedPchobject.OneHopNumberOfAllAnotherPCH = (sumOfAllOneHopInallNodes/NumberOfNodes).toFixed(3);
			sumOfAllOneHopInallNodes = 0;

		});	

		return array_of_PCH_objects;

	}

	//calculate Node Centrality
	function computeChance(array_of_PCH_objects){

		var phcChance = 0;
		
		array_of_PCH_objects.forEach(function(selectedPch){

			phcChance = FuzzyChance(selectedPch['NodeDegree'], selectedPch['Centrality'], selectedPch['OneHopNumberOfAllAnotherPCH']);

			selectedPch.NumChance = phcChance['Numerical'];
			selectedPch.DispChance = phcChance['Display'];
			
		});	

		return array_of_PCH_objects;

	}

	//comes up with a fuzzy logic chance to classify Potential cluster heads
	function FuzzyChance(degree, centrality, oneHopNumber){
		var numChance;
		var dispChance;
		var chance;

		//weights	
		var degreelow = 1.1;			// 2.3 very weak	
		var degreemid = 1.3;			// 2.5 weak
		var degreehigh = 1.6;			// 2.7 medium
		var centralityfar = 1.2;		// 2.7 medium
		var centralitymid = 1.4;		// 3	strong
		var centralityclose = 1.7;		// 3.3 very strong
		var oneHopNumberLarge = 1.1
		var oneHopNumberMedium = 1.5
		var oneHopNumberlow = 1.8


		//All possibilities
		// 3.4
		// 3.8
		// 4.1
		// 3.6
		// 4
		// 4.3
		// 3.9
		// 4.3
		// 4.6

		// 3.6
		// 4
		// 4.3
		// 3.8
		// 4.2
		// 4.5
		// 4.1
		// 4.5
		// 4.8
		
		// 3.9
		// 4.3
		// 4.6
		// 4.1
		// 4.5
		// 4.8
		// 4.5
		// 4.9
		// 5.2

		//variables
		var degree_is_low = 0;
		var degree_is_mid = 0;
		var degree_is_high = 0;
		var centrality_is_far = 0;
		var centrality_is_mid = 0;
		var centrality_is_close = 0;
		var oneHopNumber_is_large = 0;
		var oneHopNumber_is_Medium = 0;
		var oneHopNumber_is_Small = 0; 
		
		if(degree <= 0.17){
			degree_is_low = 1;
		}
		if(degree > 0.17 && degree <= 0.27){
			degree_is_mid = 1;
		}
		if(degree > 0.27){
			degree_is_high = 1;
		}
		if(centrality >= 0.00070){
			centrality_is_far = 1;
		}
		if(centrality < 0.00070  && centrality >= 0.00065){
			centrality_is_mid = 1;
		}
		if(centrality < 0.00065){
			centrality_is_close = 1;
		}
		if(oneHopNumber > 3.5){
			oneHopNumber_is_large = 1;
		}
		if(oneHopNumber <= 3.5 && oneHopNumber > 1.5){
			oneHopNumber_is_Medium = 1;
		}
		if(oneHopNumber < 1.5){
			oneHopNumber_is_Small = 1;
		}

		numChance = degreelow*degree_is_low + degreemid*degree_is_mid + degreehigh*degree_is_high + centralityfar*centrality_is_far + centralitymid*centrality_is_mid + centralityclose*centrality_is_close 
					+ oneHopNumberLarge*oneHopNumber_is_large + oneHopNumberMedium*oneHopNumber_is_Medium + oneHopNumberlow*oneHopNumber_is_Small;

		switch(numChance.toFixed(1)){
			case '3.4':
				dispChance = 'Very weak';
			break;

			case '3.6':
				dispChance = 'Very Weak';
			break;

			case '3.8':
				dispChance = 'Weak';
			break;

			case '3.9':
				dispChance = 'Little weak';
			break;

			case '4.0':
				dispChance = 'Low medium';
			break;

			case '4.1':
				dispChance = 'Low medium';
			break;

			case '4.2':
				dispChance = 'Medium';
			break;

			case '4.3':
				dispChance = 'Medium';
			break;

			case '4.5':
				dispChance = 'High medium';
			break;

			case '4.6':
				dispChance = 'Little strong';
			break;

			case '4.8':
				dispChance = 'Strong';
			break;

			case '4.9':
				dispChance = 'Very Strong';
			break;

			case '5.1':
				dispChance = 'Strongest';
			break;
		}


		chance = {Numerical: numChance, Display: dispChance}
		return chance;
	}

	//cluster the nodes based on the chance of each PCH
	function cluster(array_of_PCH_objects, range){
		var count = 1;
		var array_of_CH_objects = [];
		var phcChance = 0;
		var a = 13
		var hasBestChance = false;

		array_of_PCH_objects.forEach(function(selectedPch){
	
			array_of_PCH_objects.some(function(pchObject){
				nodeDistance = distanceBetweenNodes(selectedPch['x'], selectedPch['y'], pchObject['x'], pchObject['y']);

				if(nodeDistance < 2*range-a && nodeDistance != 0){
					hasBestChance = true;
					var chance_spch = selectedPch['NumChance'];
					var chance_pch = pchObject['NumChance'];
					var oneHopNum_spch = selectedPch['OneHopNumberOfAllAnotherPCH']
					var oneHopNum_pch = pchObject['OneHopNumberOfAllAnotherPCH']
					var nodesInRange_spch = selectedPch['numOfNodesInRange'];
					var nodesInRange_pch = pchObject['numOfNodesInRange'];
					var centrality_spch = selectedPch['Centrality'];
					var centrality_pch = pchObject['Centrality'];
					var degree_spch = selectedPch['NodeDegree'];
					var degree_pch = pchObject['NodeDegree'];
					var PCHNum_spch = selectedPch['name'].substring(3);
					var PCHNum_pch = pchObject['name'].substring(3);

					if(chance_spch < chance_pch){
						hasBestChance = false;
						return true;
					}else if(chance_spch == chance_pch){
						console.log('Nodes have equal chance');
						// if(nodesInRange_spch < nodesInRange_pch){
						// 	hasBestChance = false;
						// 	return true;
						// }else if(nodesInRange_spch == nodesInRange_pch){
						// 	console.log('Nodes have equal number of nodes');
							if(centrality_spch > centrality_pch){
								hasBestChance = false;
								return true;
							}else if(centrality_spch == centrality_pch){
								console.log('Nodes have equal centrality');
								if(degree_spch < degree_pch){
									hasBestChance = false;
									return true;
								}else if(degree_spch == degree_pch){
									console.log('Nodes have equal Node degree');
									if(PCHNum_spch < PCHNum_pch){
										hasBestChance = false;
									return true;
									}	
								}								
							}
						//}
					}
				}
			});	

			if(hasBestChance){
				
				CH_object = {name:"CH " + count, x: selectedPch['x'], y: selectedPch['y'], ArrayOfNodesInRange: selectedPch['ArrayOfNodesInRange'], numOfNodesInRange:selectedPch['numOfNodesInRange']};
				array_of_CH_objects.push(CH_object);
				drawStroke(selectedPch['x'],selectedPch['y'],range);
				drawStroke(selectedPch['x'],selectedPch['y'],2*range);
				drawStroke(selectedPch['x'],selectedPch['y'],2*range+1);
				drawfill(selectedPch['x'],selectedPch['y'],3);
				drawText("CH " + count, selectedPch['x']-10, selectedPch['y']+15);
				count++;
			}
							
		});	

		return array_of_CH_objects;		
	}


//----------------------------------------------functions as tools--------------------------------------------------------

	//Scales the field on the screen 2000 meters are equal to 800 pixels (1px = 2.5m)
	function toMeters(value){

		value = value/1.25;

		return value;
	}

	//Calculates the distance between 2 point on x and y axies
	function distanceBetweenNodes(x1, y1, x2, y2){
		var a = x1 - x2;
		var b = y1 - y2;

		var c = Math.sqrt( a*a + b*b );

		return c;
	}


	//Returns a random number between min (inclusive) and max (exclusive)
	function randBetween(min, max) {  
	  
	   var  randomNum = Math.floor(Math.random() * (max - min) + min);

	   return randomNum;
	}

	function drawfill (x,y,r){
		var c = $("#myCanvas")[0];
		var ctx = c.getContext("2d");
		ctx.beginPath();
		ctx.arc(x, y, r, 0, 2 * Math.PI);
		ctx.fill();
		ctx.closePath();
	}


	function drawStroke (x,y,r){
		var c = $("#myCanvas")[0];
		var ctx = c.getContext("2d");
		ctx.beginPath();
		ctx.arc(x, y, r, 0, 2 * Math.PI);
		ctx.stroke();
		ctx.closePath();
	}

	function drawText (text, x, y){
		var c = $("#myCanvas")[0];
		var ctx = c.getContext("2d");
		ctx.font = "12px Calibri";
		ctx.fillText(text, x, y);
	}
});