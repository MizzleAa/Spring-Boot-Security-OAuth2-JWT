import React, { Component } from 'react';
import './Profile.css';

class Profile extends Component {
    constructor(props) {
        super(props);
        console.log(props);
    }
    render() {
        return (
            <div className="profile-container">
                <div className="container">
                    <div className="profile-info">
                        <div className="profile-avatar">
                            { 
                                this.props.currentUser.information.imageUrl ? (
                                    <img src={this.props.currentUser.information.imageUrl} alt={this.props.currentUser.information.name}/>
                                ) : (
                                    <div className="text-avatar">
                                        <span>{this.props.currentUser.information.name && this.props.currentUser.information.name[0]}</span>
                                    </div>
                                )
                            }
                        </div>
                        <div className="profile-name">
                           <h2>{this.props.currentUser.information.name}</h2>
                           <p className="profile-email">{this.props.currentUser.information.email}</p>
                        </div>
                    </div>
                </div>    
            </div>
        );
    }
}

export default Profile