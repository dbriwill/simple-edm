angular.module('messageService', ['ngResource']).
factory('Message', function ($resource) {
    return $resource('message/:id', {}, {
        //'save': {method:'POST'}
    });
});
