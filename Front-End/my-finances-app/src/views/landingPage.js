import React from 'react'
import { withRouter } from 'react-router-dom'

class LadingPage extends React.Component{

    goToHomePage = () => {
        this.props.history.push("/home")
    }

    render(){
        return(
            <div className="container text-center">
                <h2 style={{marginTop: '150px'}}>Welcome to the system My Finances.</h2>
                This is your personal finance control system, click the button below to access the system.< br/>< br/>

                <div className="offset-md-4 col-md-4">
                    <button style={{width: '100%'}} 
                            onClick={this.goToHomePage} 
                            className="btn btn-success">
                        <i className="pi pi-sign-in"></i> Acess
                    </button>
                </div>
            </div>
        )
    }
}

export default withRouter(LadingPage)