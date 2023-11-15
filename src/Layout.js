import React, { useEffect, useState } from "react";
import { useNavigate, useLocation} from "react-router-dom";
import { NavLink, Outlet } from "react-router-dom";
import { v4 as uuidv4 } from 'uuid';
import './index.css';

function Layout() {
    return (
        <>
            <header>
                <span className="icon"><p id="menu-icon">&#9776;</p> </span>
                <h1>Skyward Bound</h1>
                <h5 id="caption">We'll get you where you need to go.&#9992;</h5>
            </header>
            <div className="main">
                <span className="logo">
                    <img src="/airline.png" alt="Company Logo" />
                </span>
                <div className="buttons" >
                    <button className="sign-up">Sign Up</button>
                    <button className="log-in">Log In</button>
                </div>
            </div>
        </>
    )
}

export default Layout;