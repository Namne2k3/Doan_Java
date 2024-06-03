import React, { useState } from 'react'
import "./LoginPopup.css"
import { images } from '../../assets/images'
const LoginPopup = ({ setShowLogin }) => {

    const [currentState, setCurrentState] = useState("Login")

    return (
        <div className='login-popup'>
            <form action="" className="login-popup-container">
                <div className="login-popup-title">
                    <h2>{currentState === "Login" ? "Đăng nhập" : "Đăng ký"}</h2>
                    <img src={images.cross_icon} onClick={() => setShowLogin(false)} alt="" />
                </div>
                <div className="login-popup-inputs">
                    {
                        currentState === "Login"
                            ?
                            <></>
                            :
                            <input type="text" placeholder='Tên tài khoản' required />
                    }
                    <input type="email" placeholder='Email' required />
                    <input type="password" placeholder='Mật khẩu' required />
                </div>
                <button type='submit'>
                    {
                        currentState === "Sign Up"
                            ? "Tạo tài khoản"
                            : "Đăng nhập"
                    }
                </button>
                {/* <div className="login-popup-condition">
                    <input type="checkbox" required />
                    <p>Agre</p>
                </div> */}
                {
                    currentState === "Login"
                        ?
                        <p>Tạo tài khoản mới? <span onClick={() => setCurrentState("Sign Up")}>Ở đây</span> </p>
                        :
                        <p>Đã có tài khoản? <span onClick={() => setCurrentState("Login")}>Đăng nhập ở đây</span> </p>
                }

            </form>
        </div>
    )
}

export default LoginPopup