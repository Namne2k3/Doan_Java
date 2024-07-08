import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { userService } from '../../services'
import { ToastContainer, toast } from 'react-toastify'
import { sendEmailVerify } from '../../services/EmailService'
import "./VerifyEmail.css"
import axios from 'axios'
const VerifyEmail = () => {

    const { token } = useParams()
    const navigate = useNavigate()
    const [isVerified, setIsVerified] = useState(false)
    const [isSending, setIsSending] = useState(false)
    const [email, setEmail] = useState("")

    useEffect(() => {
        const verifyEmail = async () => {
            const profileData = await userService.getUserProfile(token)
            try {
                const res = await axios.put(`http://localhost:8080/auth/verifyEmail/${profileData.data.id}`)
                if (res.data.statusCode === 200) {

                    setIsVerified(prev => true)

                } else {
                    throw new Error(res.data.message)
                }
            } catch (e) {
                toast.error(e.message)
            }
        }

        token &&
            verifyEmail()
    }, [])

    return (
        token ?
            isVerified == true ?
                <div className='verify_email_form_section'>

                    <div className='verify_email_form_container'>
                        <div className='verify_email_form_header'>

                            <h3>Xác thực email hoàn tất</h3>

                        </div>
                        <div className="verify_email_form_body">
                            <div className='verify_email_form_info'>
                                <i className="fa-solid fa-circle-check"></i>
                                <p className='mt-4'>Vui lòng quay trở lại trang chủ</p>
                            </div>
                        </div>
                        <button onClick={() => {
                            navigate("/")
                        }}>Trở về trang chủ</button>
                    </div>
                </div>
                :
                <div className='verify_email_form_section'>

                    <div className='verify_email_form_container'>
                        <div className='verify_email_form_header'>

                            <h3>Email chưa được xác thực</h3>

                        </div>
                        <div className="verify_email_form_body">
                            <div className='verify_email_form_info'>
                                <i className="fa-solid fa-circle-xmark"></i>
                                <p className='mt-4'>Vui lòng xác thực lại email</p>
                            </div>
                        </div>
                        <button onClick={() => {
                            navigate("/")
                        }}>Trở về trang chủ</button>
                    </div>
                    <ToastContainer draggable stacked autoClose={2000} hideProgressBar />
                </div>
            :
            <>
                <div className='email_recovery_form_section'>

                    <div className='email_recovery_form_container'>
                        <div className='email_recovery_form_header'>
                            <i onClick={() => {
                                navigate("/")
                            }} className="fa-solid fa-arrow-left"></i>
                            <h3>Xác thực email</h3>
                            <h3></h3>
                        </div>
                        <div className="email_recovery_form_body">
                            <form
                                onSubmit={(e) => {
                                    e.preventDefault()
                                    toast.promise(sendEmailVerify(email), {
                                        pending: "Email đang được gửi đi",
                                        error: "Có lỗi khi gửi email. Vui lòng thử lại sau"
                                    })
                                }}
                            >
                                <input value={email} onChange={(e) => setEmail(prev => e.target.value)} type="email" placeholder='Email' name='email' id='email' />
                                {
                                    isSending === false ?
                                        <button type='submit'>Tiếp theo</button>
                                        :
                                        <button disabled style={{ opacity: "0.7" }} type='submit'>Đang gửi</button>

                                }
                            </form>
                        </div>
                    </div>
                    <ToastContainer draggable stacked autoClose={2000} />
                </div>
            </>
    )
}

export default VerifyEmail