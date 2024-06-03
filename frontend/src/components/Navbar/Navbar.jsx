import React, { useContext } from 'react'
import "./Navbar.css"
import { images } from '../../assets/images'
import { useState } from 'react'
import { Link } from 'react-router-dom'
import { StoreContext } from '../../context/StoreContext'

const Navbar = ({ setShowLogin }) => {

    const [menu, setMenu] = useState("home")

    const { getTotalCartAmount } = useContext(StoreContext);

    return (
        <div className='navbar'>
            <Link to="/">
                <img src={images.logo} alt="logo" className='logo' />
            </Link>
            <ul className="navbar-menu">
                <Link to="/" onClick={() => setMenu("home")} className={menu === "home" ? "active" : ""}>Trang chủ</Link>
                <a href='#explore-menu' onClick={() => setMenu("menu")} className={menu === "menu" ? "active" : ""}>Danh mục</a>
                <a href='#footer' onClick={() => setMenu("contact-us")} className={menu === "contact-us" ? "active" : ""}>Liên hệ</a>
            </ul>
            <div className="navbar-right">
                <img src={images.search_icon} alt="search_icon" />
                <div className="navbar-search-icon">
                    <Link to="/cart">
                        <img src={images.basket_icon} alt="basket_icon" />
                    </Link>
                    <div className={getTotalCartAmount() === 0 ? "" : "dot"}>

                    </div>
                </div>
                <button onClick={() => setShowLogin(true)} className='navbar-button'>
                    Đăng nhập
                </button>
            </div>
        </div>
    )
}

export default Navbar