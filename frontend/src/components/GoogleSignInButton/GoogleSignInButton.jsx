import React, { useContext } from 'react';
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';
import { useNavigate } from 'react-router-dom';
import { StoreContext } from '../../context/StoreContext';

const GoogleSignInButton = ({ setShowLogin }) => {
    const { fetchProfileData } = useContext(StoreContext);
    const clientId = '1094118264922-622e85rqng6kd6pc93o29cjs88cd8qn4.apps.googleusercontent.com'; // Thay thế bằng Client ID của bạn
    const navigate = useNavigate()
    const onSuccess = async (res) => {
        // Gửi mã thông báo truy cập tới máy chủ để xác thực
        // console.log("Check res credentials >>> ", res);
        // const response = axios.post(`http://localhost:8080/api/v1/auth/google`, JSON.stringify({ token: res.credential }), {
        //     headers: {
        //         'Content-Type': 'application/json',
        //     },
        // })

        // const data = response.data;
        // if (data) console.log("Check login success data >>> ", data);

        fetch('http://localhost:8080/api/v1/auth/google', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ token: res.credential }),
        })
            .then((response) => response.json())
            .then(async (data) => {
                // Lưu trữ thông tin người dùng (ví dụ: JWT) và chuyển hướng người dùng
                console.log('Login success:', data);
                localStorage.setItem('token', data.token)
                await fetchProfileData()
                setShowLogin(false)
                navigate('/')
            })
            .catch((error) => {
                console.error('Login error:', error);
            });
    };

    const onFailure = (err) => {
        console.log('Login failed:', err);
    };


    return (
        <GoogleOAuthProvider clientId={clientId}>
            <GoogleLogin
                onSuccess={onSuccess}
                onError={onFailure}>
            </GoogleLogin >
        </GoogleOAuthProvider >
    );
};

export default GoogleSignInButton;