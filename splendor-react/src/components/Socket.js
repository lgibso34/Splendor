const DefaultConfig = {
    PROTOCOL: 'ws://',
    HOST: 'localhost',
    PORT: ':59001'
}

const SingleSocket = (function () {
    let instance;


    function createInstance(specIP) {
        const socket = new WebSocket(DefaultConfig.PROTOCOL + (specIP ? specIP : DefaultConfig.HOST) + DefaultConfig.PORT)
        return socket
    }

    return {
        getInstance: function(specIP) {
            if(!instance){
                instance = createInstance(specIP)
            }
            return instance
        }
    };
})();

export default SingleSocket