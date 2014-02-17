function DocumentNewController($scope, $http, $window, $location, $routeParams, Document, fileUpload) {
	
	$scope.parentNode = $routeParams.root;

	// file upload options
	$scope.fileUploadOptions = {
		url: "/document/file/upload",
		maxNumberOfFiles : 1,
		dataType: 'json'
    };

	$scope.back = function() {
		$window.history.back();
	}
	
	$scope.submit = function() {
		console.debug("submit form");
		return;
		//console.info("Uploading file...");
		Document.save($scope.document, function(document) {
			//$location.path('/');
		});
	};
}
