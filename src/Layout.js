import React, { useEffect, useState } from "react";
import { useNavigate, useLocation} from "react-router-dom";
import { NavLink, Outlet } from "react-router-dom";
import { v4 as uuidv4 } from 'uuid';
import './index.css';

function Layout() {
    const location = useLocation();
    const isOnAuthPage = location.pathname === '/signup' || location.pathname === '/login';

    return (
        <>
            <header>
                <span className="icon"><p id="menu-icon">&#9776;</p> </span>
                <h1>Skyward Bound</h1>
                <h5 id="caption">We'll get you where you need to go.&#9992;</h5>
            </header>
            <div className="main">
                {!isOnAuthPage && (
                    <>
                        <span className="logo">
                            <img src="/airline.png" alt="Company Logo" />
                        </span>
                        <div className="buttons">
                            <NavLink to="/signup" className="sign-up">Sign Up</NavLink>
                            <NavLink to="/login" className="log-in">Log In</NavLink>
                        </div>
                    </>
                )}
                <Outlet />
            </div>
        </>
    )
}

export default Layout;