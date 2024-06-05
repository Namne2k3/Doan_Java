import React, { useEffect, useState } from 'react'
import "./AddProduct.css"
import { assets } from '../../../../admin_assets/assets'
import axios from 'axios'
const AddProduct = () => {

    const BASE_URL = "http://localhost:8080"

    const [image, setImage] = useState(false)
    const [data, setData] = useState({
        name: "",
        description: "",
        price: "",
        category: "Laptop"
    })
    const onChangeHandler = (e) => {
        const name = e.target.name;
        const value = e.target.value;
        setData(data => ({ ...data, [name]: value }))
        console.log(e.target.name + ": " + e.target.value);
    }
    const onSubmitHandler = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('name', data.name)
        formData.append('description', data.description)
        formData.append('price', Number(data.price))
        formData.append('category', "665eee7ad176ea3961e606bf")
        formData.append('image', image);

        console.log("Check form data >>> ", formData);

        await handleUpload(image);

        const response = await axios.post(`${BASE_URL}/api/v1/products`, {
            "name": "MSI Bravo 15",
            "content": "THĂNG CẤP MẠNH MẼ Hãy tưởng tượng thần thú lôi điểu đang tung cánh trên không, ánh mắt sắc bén dõi theo từng chuyển động của con mồi. Logo lôi điểu được khắc trên nắp máy màu đen, cùng với các đường viền góc cạnh giúp làm nổi bật lên thiết kế mạnh mẽ và đầy phong cách. Ngay từ cái nhìn bên ngoài, các game thủ đã có thể nhận ra ngay sức mạnh của Bravo series - đồng đội đáng tin cậy trong những trận game căng thẳng. TRANG BỊ CÔNG NGHỆ TÂN TIẾN NHẤT Được sản xuất trên dây chuyền 7nm đột phá, vi xử lí di động tối đa tới AMD Ryzen™ 7 5800H và card đồ họa Radeon™ RX 5500M giúp chiếc laptop của bạn sở hữu sức mạnh sánh được với máy desktop, để bạn có trải nghiệm chơi game và đa phương tiện tuyệt vời hơn. THIẾT KẾ RIÊNG CHO CÁC GAME THỦVới đèn nền đỏ, khung bàn phím bằng kim loại và hành trính phím sâu tới 1.7mm, cho cảm giác phản hồi và độ nảy tốt hơn hẳn. Chiếc bàn phím có thiết kế công thái học này sẽ đáp ứng mọi yêu cầu của game thủ. SẴN SÀNG DI ĐỘNG VỚI CHẾ ĐỘ MODERN STANDBY Khởi động tức thời từ trạng thái nghỉ, sẵn sàng chạy hết công suất trong vòng chưa tới 1 giây, giúp bạn nhanh chóng quay trở lại game. Đăng nhập nhanh hơn & Khởi động tức thời Chế độ ngủ đông siêu tiết kiệm điện Kết nối Wi-Fi nhanh hơn & Thông báo",
            "description": "Phục kích ở nơi hiểm yếu, quan sát kĩ càng kẻ địch trước khi xuất trận mạnh mẽ, Bravo 15 đã sẵn sàng cho chiến trường game rực lửa. Kết hợp giữa vi xử lí AMD Ryzen™ 5000 series và card đồ họa AMD Radeon™ RX 5500M, Bravo 15 sẽ làm thỏa mãn mọi game thủ. Với giải pháp tản nhiệt độc quyền của MSI – Cooler Boost 5, CPU và GPU sẽ thoải mái phát huy được tối đa hiệu năng. Màn hình chuyên game 144Hz mang tới hình ảnh sắc nét và mượt mà, giúp các game thủ bắt kịp mọi tình huống. Tận dụng sức mạnh của Bravo 15 và tung hoành trên chiến trường ảo, giống như lôi điểu trong truyền thuyết!",
            "price": 20000000,
            "stock_quantity": 156,
            "category": {
                "id": "665dec1a1c0c82440032af8e"
            },
            "watchCount": 12000,
            "brand": {
                "id": "665ed539fe581060c7aadd87"
            },
            "image": image.name,
            "images": ["https://storage-asset.msi.com/global/picture/image/feature/nb/AMD/Bravo15-5000/bravo-nb.png", "https://storage-asset.msi.com/global/picture/image/feature/nb/AMD/Bravo15-5000/bravo-nb.png", "https://storage-asset.msi.com/global/picture/image/feature/nb/AMD/Bravo15-5000/bravo-nb.png"]
        })
        console.log("Check response >>> ", response);
        if (response.data.statusCode !== 200) {
            setData({
                name: "",
                description: "",
                price: "",
                category: "Laptop"
            })
            setImage(false);
        }
    }

    const handleUpload = async () => {
        if (!image) {
            alert("Please select a file.");
            return;
        }

        const formData = new FormData();
        formData.append("image", image);

        try {
            const response = await axios.post(`${BASE_URL}/api/v1/upload`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            if (response.data) {
                console.log("Check response >>> ", response);
            }
        } catch (error) {
            console.error("Error uploading file:", error);
            alert("Error uploading file. Please try again.");
        }
    }
    useEffect(() => {
        // console.log(data);
    }, [data])

    // 4:00:00

    const imagePath = process.env.PUBLIC_URL + '/images/';


    return (
        <div className='add'>
            <form onSubmit={onSubmitHandler} className="flex-col">
                <div className="add-img-upload flex-col">
                    <p>Thêm ảnh sản phẩm</p>
                    <label htmlFor="image">
                        <img src={image ? URL.createObjectURL(image) : assets.upload_area} alt="upload_area_img" />
                    </label>
                    <input name='image' accept='image/*' onChange={(e) => {
                        setImage(e.target.files[0])
                        console.log(e.target.files[0]);
                    }} type="file" id='image' hidden required />
                </div>
                <div className="add-product-name flex-col">
                    <p>Tên ản phẩm</p>
                    <input onChange={onChangeHandler} value={data.name} type="text" name='name' placeholder='Nhập tên sản phẩm' />
                </div>
                <div className="add-product-description flex-col">
                    <p>Mô tả sản phẩm</p>
                    <textarea onChange={onChangeHandler} value={data.description} name="description" rows="6" placeholder='Thêm nội dung sản phẩm'></textarea>
                </div>
                <div className="add-category-price">
                    <div className="add-category flex-col">
                        <p>Danh mục sản phẩm</p>
                        <select onChange={onChangeHandler} value={data.category} name="category">
                            <option value="Laptop">Laptop</option>
                            <option value="Mobile Phone">Mobile Phone</option>
                            <option value="Smart Watch">Smart Watch</option>
                        </select>
                    </div>

                    <div className="add-price flex-col">
                        <p>Giá bán sảm phẩm</p>
                        <input onChange={onChangeHandler} value={data.price} type="number" name='price' placeholder='Giá bán sản phẩm' />
                    </div>
                </div>
                <button className='add-btn' type='submit'>Thêm sản phẩm</button>
            </form>
        </div>
    )
}

export default AddProduct