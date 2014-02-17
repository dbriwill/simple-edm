angular.module('messageService', [ 'ngResource' ]).factory('Message', function($resource) {
	return $resource('message/:id', {}, {
	//'save': {method:'POST'}
	});
});

angular.module('libraryService', [ 'ngResource' ]).factory('Library', function($resource) {
	return $resource('library/:id', {}, {});
});

angular.module('nodeService', [ 'ngResource' ]).factory('Node', function($resource) {
	return $resource('node/:nodepath', {}, {});
});

angular.module('documentService', [ 'ngResource' ]).factory('Document', function($resource) {
	return $resource('document/:nodepath', {}, {});
});

