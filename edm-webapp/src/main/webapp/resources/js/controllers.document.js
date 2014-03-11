function defineFileUploadOptionForScope($scope) {
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
}

function DocumentNewController($scope, $http, $window, $location, $routeParams, Node, Document, fileUpload, notification) {

	$scope.submitFormDisabled = true;
	$scope.temporaryFileToken = null;
	$scope.document = {};
	
	// get parent node id and save it
	$http.get('/node/path/' + $routeParams.root).success(function(response, status, headers, config) {
		$scope.document.parentId = response.id;
	});

	defineFileUploadOptionForScope($scope);

	$scope.back = function() {
		$window.history.back();
	}
	
	$scope.submitForm = function() {
		console.log("submit form");
		
		$scope.submitFormDisabled = true;
		
		Document.save($scope.document, function(document) {
			if (document.id) { // save success
				notification.add('INFO', "Le document a bien été sauvegardé");
				$location.path('/node/').search('path', $routeParams.root);
			}
			else {
				notification.add('ERROR', "Une erreur s'est produite lors de la sauvegarde du document");
			}
		});
	};
}


function DocumentEditController($scope, $http, $window, $location, $routeParams, Node, Document, fileUpload, notification) {

	$scope.submitFormDisabled = false; // it's not necessary to update file
	$scope.temporaryFileToken = null;

	// get parent node id and save it
	$http.get('/node/path/' + $routeParams.path).success(function(response, status, headers, config) {
		$scope.document = response;
	});

	defineFileUploadOptionForScope($scope);

	$scope.back = function() {
		$window.history.back();
	}
	
	$scope.submitForm = function() {
		console.log("submit form");
		
		$scope.submitFormDisabled = true;
		
		Document.save($scope.document, function(document) {
			if (document.id) { // save success
				notification.add('INFO', "Le document a bien été sauvegardé");
				$location.path('/node/').search('path', $routeParams.path);
			}
			else {
				notification.add('ERROR', "Une erreur s'est produite lors de la sauvegarde du document");
			}
		});
	};
}

function DocumentSearchController($scope, $http, $location, $routeParams) {
	$scope.searchedPattern = $routeParams.q;
	
	$scope.getNodePath = function(node) {
		$http.get('/node/getPath/' + node.id).success(function(response, status, headers, config) {
			node.path = response;
		});
	}
	
	$scope.searchSubmit = function() {
		console.debug("Search : " + $scope.searchedPattern);
		$location.path('/document/search').search({
			'q' : $scope.searchedPattern
		});
	}
	
	$http.get('/document?q=' + $scope.searchedPattern).success(function(response, status, headers, config) {
		$scope.documents = response;
	});
}

