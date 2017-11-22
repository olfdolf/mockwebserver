angular.module('app').controller('MappingsController', function($scope, $http) {

	$http.get("/list/mappings").then(function(response) {
		$scope.mappings = response.data;
		$scope.mappings.forEach(function(value, index, array) {
			array[index].responseBody = window.atob(array[index].responseBody);
		})
	});

	$scope.remove = function(id) {
		$http.get("/remove/mapping/" + id).then(function(response) {
			if (response.data == true) {
				$scope.mappings.forEach(function(value, index, array) {
					if (array[index].id == id) {
						array.splice(index, 1);
					}
				});
			} else {
				alert("could not remove");

			}
		});
	};
});