import React, { useContext } from 'react'
import "./Navbar.css"
import { images } from '../../assets/images'
import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { StoreContext } from '../../context/StoreContext'
import { useEffect } from 'react'
import { userService } from '../../services'


const Navbar = ({ setShowLogin }) => {

    const [menu, setMenu] = useState("home")
    const { getTotalCartAmount } = useContext(StoreContext)
    const [isSearching, setIsSearching] = useState(false)
    const [userProfile, setUserProfile] = useState({})
    const navigate = useNavigate();

    const logout = () => {
        userService.logout();
        setUserProfile({});
        window.location.href = "/"
    }

    useEffect(() => {

        const fetchProfileData = async () => {
            try {

                const token = localStorage.getItem('token'); // Retrieve the token from localStorage
                if (!token) {
                    navigate("/")
                }
                const response = await userService.getUserProfile(token);
                // console.log("Check res >>> ", response.data);

                setUserProfile(response.data);
            } catch (err) {

            }
        }

        fetchProfileData();
    }, [localStorage.getItem('role')])

    return (
        <div className='navbar'>
            <Link to="/">
                <img src={images.logo} alt="logo" className='logo' />
            </Link>
            {
                isSearching === false
                    ?
                    <ul className="navbar-menu">
                        <Link to="/" onClick={() => setMenu("home")} className={menu === "home" ? "active" : ""}>Trang chủ</Link>
                        <a href='#explore-menu' onClick={() => setMenu("menu")} className={menu === "menu" ? "active" : ""}>Danh mục</a>
                        <a href='#footer' onClick={() => setMenu("contact-us")} className={menu === "contact-us" ? "active" : ""}>Liên hệ</a>
                    </ul>
                    :
                    <div className='navbar-search'>
                        <input type='text' placeholder='Tìm kiếm sản phẩm' />
                    </div>
            }
            <div className="navbar-right">
                <img onClick={() => setIsSearching(!isSearching)} src={images.search_icon} alt="search_icon" />
                <div className="navbar-search-icon">
                    <Link to="/cart">
                        <img src={images.basket_icon} alt="basket_icon" />
                    </Link>
                    <div className={getTotalCartAmount() === 0 ? "" : "dot"}>

                    </div>
                </div>
                {
                    localStorage.getItem('token') != null
                        ?
                        <>
                            <Link to="/profile">
                                <img src={images.profile_icon} alt="profile_icon" />
                            </Link>
                            <button onClick={logout} className='navbar-button'>
                                Đăng xuất
                            </button>
                        </>
                        :
                        <>
                            <button onClick={() => setShowLogin(true)} className='navbar-button'>
                                Đăng nhập
                            </button>
                        </>
                }
                {
                    userProfile.role === "ADMIN"
                    &&
                    <Link to="/admin">
                        <img style={{ maxWidth: "34px" }} src={images.admin_icon} alt="admin_icon" />
                    </Link>
                }
            </div>
        </div>
    )
}

export default Navbar