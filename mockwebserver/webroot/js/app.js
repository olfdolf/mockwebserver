var appModule = angular.module("app", [ "ngRoute" ]);
appModule.config(function($routeProvider) {
	$routeProvider.when("/addmapping", {
		templateUrl : "/views/addmapping.htm",
		controller : "AddMappingController"
	}).when("/editmapping/:id", {
		templateUrl : "/views/editmapping.htm",
		controller : "EditMappingController"
	}).when("/mappings", {
		templateUrl : "/views/mappings.htm",
		controller : "MappingsController"
	}).otherwise({
		templateUrl : "/views/logs.htm",
		controller : "LogsController"
	});
});