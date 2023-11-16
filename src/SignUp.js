import React, { useEffect, useState } from "react";
import { useNavigate, useLocation} from "react-router-dom";
import { NavLink, Outlet } from "react-router-dom";
import { v4 as uuidv4 } from 'uuid';
import './index.css';

function SignUp() {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        // Add other fields as necessary
    });

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        // Implement the logic to send data to the backend
        console.log('Form submitted:', formData);
    };

    return (
        <div>
            <h2 className="title">Sign Up Page</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="name"
                    placeholder="Full Name"
                    value={formData.name}
                    onChange={handleChange}
                />
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
                {/* Add other fields as necessary */}
                <button type="submit">Sign Up</button>
            </form>
        </div>
    );
}

export default SignUp;