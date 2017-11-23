angular.module('app').controller('AddMappingController',
		function($scope, $http, $routeParams, $location) {

			$scope.mapping = {};

			$scope.save = function() {
				var ob = {};
				ob.statusCode = $scope.mapping.statusCode;
				ob.id = 0;
				ob.responseBody = window.btoa($scope.mapping.responseBody);
				ob.url = $scope.mapping.url;
				ob.contentType = $scope.mapping.contentType;

				$http.put("/add/mapping", ob).then(function(response) {
					if (response.data == true) {
						$location.path("/mappings");
					} else {
						alert("Error");
					}
				})
				console.log(ob);

			};

		});