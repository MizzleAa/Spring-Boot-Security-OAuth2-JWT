import React, { Component } from 'react';
import './NotFound.css';
import { Link } from 'react-router-dom';

class NotFound extends Component {
    render() {
        return (
            <div className="page-not-found">
                <h1 className="title">
                    404
                </h1>
                <div className="desc">
                    해당 페이지는 찾을 수 없습니다..
                </div>
                <Link to="/"><button className="go-back-btn btn btn-primary" type="button">Go Back</button></Link>
            </div>
        );
    }
}

export default NotFound;