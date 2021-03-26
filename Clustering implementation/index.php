<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Clustering Algorithm</title>
	<link rel="stylesheet" media="all" href="assets/css/bootstrap.min.css"> <!-- Using bootstrap 4.0 -->
	<link rel="stylesheet" media="all" href="assets/css/style.css">
	<script type="text/javascript" src="assets/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="assets/js/bootstrap.min.js"></script> <!-- Using bootstrap 4.0 -->
	<script type="text/javascript" src="assets/js/main.js"></script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-3 col_elem">
				<div class="main_wrapper">
					<h2>Clustering Algorithm</h2>
					<p class="type_label">
						Variables
					</p>
					<form>
						<div>
							<p>Feild dimentions in meters</p>
							<input id="height_input" type="number" name="height" placeholder="length" value = "1000"/>
							<input id="width_input" type="number" name="width" placeholder="Width" value = "1800"/>
							<p>Number of Sensors</p>
							<!-- <input id="sensorNum_input" type="number" name="SensorNum" value = "3000"/> -->
							<select id="sensorNum_input">
								<option value="3000">3000</option>
								<!-- <option value="2000">2000</option>
								<option value="1000">1000</option> -->
							</select>
							<p>Nodes range in meters</p>
							<select id="range_input">
								<option value="200">200</option>
<!-- 							<option value="100">100</option>
								<option value="300">300</option> -->
							</select>
							<p>Min distance between nodes in meters</p>
							<select id="distance_input">
								<option value="10">10</option>
								<option value="15">15</option>
								<option value="20">20</option>
							</select>
						</div>
						<div class="btns_group">
							<div id="OK_btn" name="OK" class="button">OK</div>
							<div id="Refresh_btn" name="Refresh" class="buttonRefresh">Refresh</div>
						</div>
					</form> 
					<div id="resluts">
						
					</div>
				</div>
			</div>
			
			<div class="col-9 col_elem">
				<div class="main_wrapper">
					<h2>Clustering Algorithm</h2>
					<p class="type_label">
						Field
					</p>
					<canvas id="myCanvas" width="5000" height="5000"></canvas>
					<div id='hist'>
						
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>