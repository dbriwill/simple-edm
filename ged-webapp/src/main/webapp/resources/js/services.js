angular.module('messageService', ['ngResource']).
factory('Message', function ($resource) {
    return $resource('message/:id', {}, {
        //'save': {method:'POST'}
    });
});

angular.module('libraryService', ['ngResource']).
factory('Library', function ($resource) {
    return $resource('library/:id', {}, {
    });
});

