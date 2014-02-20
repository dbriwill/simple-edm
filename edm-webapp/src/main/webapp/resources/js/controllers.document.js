
function DocumentNewController($scope, $http, $window, $location, $routeParams, Node, Document, fileUpload) {

	$scope.submitFormDisabled = true;
	$scope.temporaryFileToken = null;
	$scope.document = {};
	
	// get parent node id and save it
	$http.get('/node/path/' + $routeParams.root).success(function(response, status, headers, config) {
		$scope.document.parentId = response.id;
	});

	// file upload options data-file-upload="options"
	$scope.fileUploadOptions = {
		url : '/document/upload',
		paramName : 'file',
		maxNumberOfFiles : 1,
		autoUpload : true,
		add: function (e, data) {
			console.info("Upload is starting");
			$scope.submitFormDisabled = true;
			$scope.file = data.files[0];
			$.blueimp.fileupload.prototype.options.add.call(this, e, data);
	    },
	    done: function(e, data) {
	    	$scope.document.temporaryFileToken = data.result.temporaryFileToken;
	    	console.info("Upload is over, token is " + $scope.document.temporaryFileToken);
	    	$scope.submitFormDisabled = false;
	    }
    };

	$scope.back = function() {
		$window.history.back();
	}
	
	$scope.submitForm = function() {
		console.log("submit form");
		
		$scope.submitFormDisabled = true;
		
		Document.save($scope.document, function(document) {
			if (document.id) { // save success
				$location.path('/node/').search('path', $routeParams.root);
			}
			else { // TODO : user failed feedback
				
			}
		});
	};
}
