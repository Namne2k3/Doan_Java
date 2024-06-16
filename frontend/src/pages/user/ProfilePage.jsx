import React, { useContext } from 'react'
import { userService } from '../../services'
import { Link } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'
import { StoreContext } from '../../context/StoreContext'

const ProfilePage = () => {

    const navigate = useNavigate();

    const { profileInfo } = useContext(StoreContext);

    const logout = () => {
        userService.logout();
        navigate("/")
    }
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