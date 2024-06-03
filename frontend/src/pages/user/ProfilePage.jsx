import React, { useEffect, useState, useContext } from 'react'
import { userService } from '../../services'
import { Link } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'
import { StoreContext } from '../../context/StoreContext'

const ProfilePage = () => {

    const navigate = useNavigate();

    const [profileInfo, setProfileInfo] = useState({})

    const fetchProfileData = async () => {
        try {

            const token = localStorage.getItem('token'); // Retrieve the token from localStorage
            if (!token) {
                navigate("/login")
            }
            const response = await userService.getUserProfile(token);

            setProfileInfo(response.user);

        } catch (err) {
            console.error('Error fetching profile information:', err);
        }
    }

    const logout = () => {
        userService.logout();
        navigate("/login")
    }

    useEffect(() => {
        fetchProfileData();
    }, [])

    return (
        <div className="profile-page-container">
            <h2>Profile Information</h2>
            <p>Name: {profileInfo.username}</p>
            <p>Email: {profileInfo.email}</p>
            {profileInfo.role === "ADMIN" && (
                <button>
                    <Link to={`/update-user/${profileInfo.id}`}>
                        Update This Profile
                    </Link>
                </button>
            )}
            <button onClick={logout}>
                Logout
            </button>
        </div>
    )
}

export default ProfilePage