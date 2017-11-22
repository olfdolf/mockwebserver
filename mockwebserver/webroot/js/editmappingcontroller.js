angular.module('app').controller(
		'EditMappingController',
		function($scope, $http, $routeParams, $location) {

			$http.get("/get/mapping/" + $routeParams.id).then(
					function(response) {
						$scope.mapping = response.data;
						$scope.mapping.responseBody = window
								.atob($scope.mapping.responseBody);
					});

			$scope.save = function() {
				var ob = {};
				ob.statusCode = $scope.mapping.statusCode;
				ob.id = $scope.mapping.id;
				ob.responseBody = window.btoa($scope.mapping.responseBody);
				ob.mapping = $scope.mapping.mapping;
				ob.contentType = $scope.mapping.contentType;

				$http.put("/edit/mapping", ob).then(function(response) {
					if (response.data == true) {
						$location.path("/mappings");
					} else {
						alert("Error");
					}

				})
				console.log(ob);

			};

		});