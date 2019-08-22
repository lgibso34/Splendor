import React, {Component} from 'react'
import SingleSocket from './Socket';

class SplendorClient extends Component {

    render = () => {
        return(
            <div>
                <button type="button" onClick={(e) => this.registerSocket(e)}>click here</button>
            </div>
        )
    }


    registerSocket(e) {
        let self = this
        this.socket = SingleSocket.getInstance()

        this.socket.onmessage = (response) => {
            console.log(response.data)
        }
    }
}

export default SplendorClient