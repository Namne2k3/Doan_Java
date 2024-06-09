import React, { useContext } from 'react'
import "./Navbar.css"
import { images } from '../../assets/images'
import { useState } from 'react'
import { Link } from 'react-router-dom'
import { StoreContext } from '../../context/StoreContext'
import { useEffect } from 'react'
import { userService } from '../../services'


const Navbar = ({ setShowLogin }) => {

    const [menu, setMenu] = useState("home")
    const { getTotalCartAmount } = useContext(StoreContext)
    const [isSearching, setIsSearching] = useState(false)

    const logout = () => {
        userService.logout();
        window.location.href = "/"
    }

    const { profileInfo } = useContext(StoreContext);

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
                    profileInfo.role === "ADMIN"
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