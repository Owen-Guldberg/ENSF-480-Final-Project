import React, { useEffect, useState } from "react";
import { useNavigate, useLocation} from "react-router-dom";
import { NavLink, Outlet } from "react-router-dom";
import { v4 as uuidv4 } from 'uuid';
import './index.css';

function Login() {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        // Implement the logic to verify user credentials
        console.log('Login Form submitted:', formData);
    };

    return (
        <div>
            <h2 className="title">Log In Page</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={formData.email}
                    onChange={handleChange}
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                />
                <button type="submit">Log In</button>
            </form>
        </div>
    );
}

export default Login;