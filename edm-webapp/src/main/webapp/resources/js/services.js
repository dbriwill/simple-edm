angular.module('messageService', [ 'ngResource' ]).factory('Message', function($resource) {
	return $resource('message/:id', {}, {
	//'save': {method:'POST'}
	});
});

angular.module('nodeService', [ 'ngResource' ]).factory('Node', function($resource) {
	return $resource('node/:id', {}, {});
});

angular.module('libraryService', [ 'ngResource' ]).factory('Library', function($resource) {
	return $resource('library/:id', {}, {});
});

angular.module('directoryService', [ 'ngResource' ]).factory('Directory', function($resource) {
	return $resource('directory/:id', {}, {});
});

angular.module('documentService', [ 'ngResource' ]).factory('Document', function($resource) {
	return $resource('document/:id', {}, {});
});
