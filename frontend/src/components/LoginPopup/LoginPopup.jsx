import React, { useState } from 'react'
import "./LoginPopup.css"
import { images } from '../../assets/images'
import { userService } from '../../services'
import { useNavigate } from "react-router-dom";
const LoginPopup = ({ setShowLogin }) => {

    const [currentState, setCurrentState] = useState("Login")
    const [email, setEmail] = useState('')
    const [username, setUserName] = useState('')
    const [phone, setPhone] = useState('')
    const [address, setAddress] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const navigate = useNavigate();



    const handleSubmitLogin = async (e) => {
        e.preventDefault();

        try {
            const userData = await userService.login(email, password)
            console.log(userData)
            if (userData.token) {
                localStorage.setItem('token', userData.token)
                localStorage.setItem('role', userData.role)
                setShowLogin(false)
                window.location.href = "/"
            } else {
                setError(userData.message)
            }

        } catch (error) {
            console.log(error)
            setError(error.message)
            setTimeout(() => {
                setError('');
            }, 5000);
        }
    }

    const handleSubmitRegister = async (e) => {
        e.preventDefault();
        try {
            // Call the register method from UserService

            const token = localStorage.getItem('token')

            const userData = await userService.register({
                username: username,
                email: email,
                password: password,
                address: address,
                phone: phone,
            }, token);
            if (userData.data) {
                setShowLogin(false)
                alert('User registered successfully');
                localStorage.setItem('token', userData.token)
                localStorage.setItem('role', userData.role)
                navigate("/")
            }

        } catch (error) {
            console.error('Error registering user:', error);
            alert('An error occurred while registering user');
        }
    };

    return (
        <div className='login-popup'>
            {
                currentState === "Login"
                    ?
                    <form onSubmit={handleSubmitLogin} className="login-popup-container">
                        <div className="login-popup-title">
                            <h2>{currentState === "Login" ? "Đăng nhập" : "Đăng ký"}</h2>
                            <img src={images.cross_icon} onClick={() => setShowLogin(false)} alt="" />
                        </div>
                        <div className="login-popup-inputs">
                            {
                                currentState === "Sign Up"
                                &&
                                <>
                                    <input value={username} onChange={(e) => setUserName(e.target.value)} type="text" placeholder='Tên tài khoản' required />
                                    <input value={phone} onChange={(e) => setPhone(e.target.value)} type="tel" placeholder='Số điện thoại' required />
                                    <input value={address} onChange={(e) => setAddress(e.target.value)} type="text" placeholder='Địa chỉ' required />
                                </>
                            }
                            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder='Email' required />
                            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder='Mật khẩu' required />
                        </div>
                        <button type='submit'>
                            {
                                currentState === "Sign Up"
                                    ? "Tạo tài khoản"
                                    : "Đăng nhập"
                            }
                        </button>
                        {
                            currentState === "Login"
                                ?
                                <p>Tạo tài khoản mới? <span onClick={() => setCurrentState("Sign Up")}>Ở đây</span> </p>
                                :
                                <p>Đã có tài khoản? <span onClick={() => setCurrentState("Login")}>Đăng nhập ở đây</span> </p>
                        }

                    </form>
                    :
                    <form onSubmit={handleSubmitRegister} className="login-popup-container">
                        <div className="login-popup-title">
                            <h2>Đăng ký</h2>
                            <img src={images.cross_icon} onClick={() => setShowLogin(false)} alt="" />
                        </div>
                        <div className="login-popup-inputs">
                            <input value={username} onChange={(e) => setUserName(e.target.value)} type="text" placeholder='Tên tài khoản' required />
                            <input value={phone} onChange={(e) => setPhone(e.target.value)} type="tel" placeholder='Số điện thoại' required />
                            <input value={address} onChange={(e) => setAddress(e.target.value)} type="text" placeholder='Địa chỉ' required />
                            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder='Email' required />
                            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder='Mật khẩu' required />
                        </div>
                        <button type='submit'>Tạo tài khoản</button>
                        <p>Đã có tài khoản? <span onClick={() => setCurrentState("Login")}>Đăng nhập ở đây</span> </p>

                    </form>

            }
        </div>
    )
}

export default LoginPopup