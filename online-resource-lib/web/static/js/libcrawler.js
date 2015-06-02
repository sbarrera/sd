
(function(module, $){

    module.controller('UserInterfaceController', function($scope, $http, $q){

        var resourceTypes = ['AUDIO', 'VIDEO', 'BOOK'];

        var Server = function(host, port, audio, video, books){
            this.host = host;
            this.port = port;
            this.AUDIO = audio;
            this.VIDEO = video;
            this.BOOK = books;
        };

        var Resource = function(host, name, desc, type, cost, link){
            this.host = host;
            this.name = name;
            this.desc = desc;
            this.type = type;
            this.cost = cost;
            this.link = link;
        };

        $scope.servers = [];
        /* $scope.kinds = {
            AUDIO: [],
            VIDEO: [],
            BOOK: []
        }; */
        $scope.resources = [];

        $scope.iconResources = {
            AUDIO: 'glyphicon glyphicon-cd',
            VIDEO: 'glyphicon glyphicon-film',
            BOOK: 'glyphicon glyphicon-book'
        };

        $scope.nameToSearch = '';

        var promise = $http.get("static/libraries.json");
        promise.success(function(servers){
            $.each(servers, function(i, server){
                var host = server.host;
                var port = server.port;
                var promises = [];

                $scope.servers.push(new Server(host, port, 0, 0, 0));

                $.each(resourceTypes, function(j, type){
                    var url = "http://" + host + ":" + port + "/llibreria/API/" + type + "/cataleg";
                    var deferred = $q.defer();
                    promises.push(deferred.promise);

                    var promise = $http.get(url)
                    promise.success(function(resources){
                        $scope.servers[i][type] = resources.length;
                        var promises = [];

                        $.each(resources, function(i, resource){
                            //var type = $scope.kinds[type];
                            //type.push(resource);
                            var name = resource.NAME;
                            var desc = resource.DESC;
                            var url = "http://" + host + ":" + port + "/llibreria/API/" + type + "/item/" + name;
                            var deferred = $q.defer();
                            promises.push(deferred.promise);

                            var promise = $http.get(url);
                            promise.success(function(resource){
                                var price = parseInt(resource.PRICE) || -1;
                                var link = "http://" + host + ":" + port + resource.LINK;
                                $scope.resources.push(new Resource(host, name, desc, type, price, link));
                                deferred.resolve();
                            });

                        });

                        $q.all(promises).then(function(){
                            deferred.resolve();
                        });
                    });
                });

                $q.all(promises).then(function(){
                    console.log('all resources loaded');
                });
            });
        });
    });

})(angular.module('crawler', []), jQuery);