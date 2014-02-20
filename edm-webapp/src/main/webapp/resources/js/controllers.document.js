
function DocumentNewController($scope, $http, $window, $location, $routeParams, Node, Document, fileUpload) {

	$scope.submitFormDisabled = true;
	$scope.temporaryFileToken = null;
	$scope.document = {};
	
	// get parent node id and save it
	Node.get({
		nodepath : $routeParams.root
	}, function(response) {
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
		
		// TODO : form validation
//		var formValid = newDocumentForm.$valid;
//		if (! formValid) {
//			console.log("Fom is not valid");
//			return false;
//		}

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
