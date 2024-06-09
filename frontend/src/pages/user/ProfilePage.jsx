import React, { useEffect, useState, useContext } from 'react'
import { userService } from '../../services'
import { Link } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'
import { StoreContext } from '../../context/StoreContext'

const ProfilePage = () => {

    const navigate = useNavigate();

    // const { setProfileInfo } = useContext(StoreContext);
    const [userProfile, setUserProfile] = useState({})

    const logout = () => {
        userService.logout();
        navigate("/")
    }

    useEffect(() => {
        const fetchProfileData = async () => {
            try {

                const token = localStorage.getItem('token'); // Retrieve the token from localStorage
                if (!token) {
                    navigate("/")
                    return;
                }
                const response = await userService.getUserProfile(token);
                // console.log("Check res >>> ", response.data);

                // setProfileInfo(response.data);
                setUserProfile(response.data);

            } catch (err) {
                console.error('Error fetching profile information:', err);
                navigate("/")
            }
        }
        fetchProfileData()
    }, [])

    return (
        <div className="profile-page-container">
            <h2>Profile Information</h2>
            <p>Name: {userProfile.username}</p>
            <p>Email: {userProfile.email}</p>
            {userProfile.role === "ADMIN" && (
                <button>
                    <Link to={`/update-user/${userProfile.id}`}>
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