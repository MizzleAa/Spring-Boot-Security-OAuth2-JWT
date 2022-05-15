import React, { Component } from 'react';
import './Home.css';
import springReactImage from '../img/spring-boot-and-react-js.png';

class Home extends Component {
    render() {
        return (
            <div className="home-container">
                <div className="container">
                    <img className="home-image" src={springReactImage} alt="Naver" />
                    <h1 className="home-title">Sample OAuth2 & JWT</h1>
                    <span className="home-footer">Copyright © 2022 Mizzle Inc. Policy Edit page on GitHub</span>
                </div>
            </div>
        )
    }
}

export default Home;