angular.module('app').controller('LogsController', function($scope, $http) {
	$http.get("/list/logs").then(function(response) {
		$scope.logs = response.data;
		$scope.logs.forEach(function(value, index, array) {
			array[index].requestBody = window.atob(array[index].requestBody);
			array[index].responseBody = window.atob(array[index].responseBody);
		})
	});
});