import React, { useEffect, useState } from 'react'
import "./AddProduct.css"

import { assets } from '../../../../admin_assets/assets'

import axios from 'axios'
import { CKEditor } from '@ckeditor/ckeditor5-react';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';

const AddProduct = () => {

    const BASE_URL = "http://localhost:8080"

    const [image, setImage] = useState(false)

    const [brands, setBrands] = useState([]);

    const [images, setImages] = useState([]);

    const [categories, setCategories] = useState([]);

    const [data, setData] = useState({
        name: "",
        content: "",
        description: "",
        price: "",
        category: "665eee91d176ea3961e606c0",
        brand: "665ed539fe581060c7aadd87",
        quantity: "",
        watchCount: "0",
        graphic: "",
        cpu: "",
        ram: "8",
        ssd: "512",
        panel: "IPS",
        screen_size: "15",
        brightness: "",
        resolution: "1920 x 1080",
        weight: ""
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
        formData.append('content', data.content)
        formData.append('description', data.description)
        formData.append('price', Number(data.price))
        formData.append('category', data.category)
        formData.append('brand', data.brand)
        formData.append('quantity', Number(data.quantity))
        formData.append('watchCount', Number(data.watchCount))
        formData.append('graphic', data.graphic)
        formData.append('cpu', data.cpu)
        formData.append('ram', Number(data.ram))
        formData.append('ssd', Number(data.ssd))
        formData.append('panel', data.panel)
        formData.append('screen_size', data.screen_size)
        formData.append('brightness', Number(data.brightness))
        formData.append('weight', Number(data.weight))
        formData.append('resolution', data.resolution)
        formData.append('image', image.name);
        formData.append('images', images.map((item, index) => {
            return (
                item.name
            )
        }));

        await handleUpload();

        const response = await axios.post(`${BASE_URL}/api/v1/products`, formData)
        console.log("Check response >>> ", response.data);
        if (response.data.statusCode !== 200) {
            setImage(false);
        }
    }

    const handleUpload = async () => {
        if (!image && images.length === 0) {
            alert("Please select a file.");
            return;
        }

        const formData = new FormData();
        if (image) formData.append("image", image);
        images.forEach((img, index) => {
            formData.append(`images`, img); // Sử dụng cùng tên 'images' cho các file nhiều ảnh
        });

        try {
            const response = await axios.post(`${BASE_URL}/api/v1/upload`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });

            if (response.status !== 200) {
                alert("Error uploading files. Please try again.");
            }
        } catch (error) {
            console.error("Error uploading file:", error);
            alert("Error uploading file. Please try again.");
        }
    }
    useEffect(() => {

        const fetchBrands = async () => {
            try {
                const response = await axios.get(`${BASE_URL}/api/v1/brands`);
                if (response.data) {
                    setBrands(response.data.dataList);
                }

            } catch (err) {
                console.error("Error get brands data:", err);
            }
        }

        const fetchCategory = async () => {
            try {
                const response = await axios.get(`${BASE_URL}/api/v1/categories`);
                if (response.data) {
                    setCategories(response.data.dataList);
                }

            } catch (err) {
                console.error("Error get categories data:", err);
            }
        }


        console.log(data);
        fetchCategory();
        fetchBrands();
    }, [data])

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

                <div className="add-img-upload flex-col">
                    <p>Thêm nhiều hình ảnh sản phẩm</p>
                    <label htmlFor="images">

                        {
                            Array(...images).length !== 0
                                ?
                                <div className="images-container">
                                    {
                                        Array(...images).map((item, index) =>
                                            <div key={`img_${index}`} className="img-container">
                                                <img src={URL.createObjectURL(item)} alt="upload_area_img" />
                                            </div>
                                        )
                                    }
                                </div>
                                :
                                <img src={assets.upload_area} alt="upload_area_img" />
                        }

                    </label>
                    <input multiple name='images' accept='image/*' onChange={(e) => setImages(Array(...e.target.files))} type="file" id='images' hidden />
                </div>

                <div className="add-product-name flex-col">
                    <p>Tên sản phẩm</p>
                    <input onInput={onChangeHandler} value={data.name} type="text" name='name' placeholder='Nhập tên sản phẩm' />
                </div>

                <div className="add-product-name flex-col">
                    <p>Mô tả phẩm</p>
                    <input onInput={onChangeHandler} value={data.description} type="text" name='description' placeholder='Nhập mô tả sản phẩm' />
                </div>

                <div className="add-product-description flex-col">
                    <p>Nội dung sản phẩm</p>
                    {/* <textarea onInput={onChangeHandler} value={data.content} name="content" rows="6" placeholder='Thêm nội dung sản phẩm'></textarea> */}
                    <CKEditor
                        editor={ClassicEditor}
                        config={{
                            ckfinder: {
                                // Upload the images to the server using the CKFinder QuickUpload command.
                                uploadUrl: `${BASE_URL}/api/v1/ckeditor/upload`
                            }
                        }}
                        data={"<p>Hello from CKEditor 5!</p>"}
                        onChange={() => {
                            // console.log(document.getElementsByClassName("ck-content")[0].innerHTML)
                            setData({ ...data, content: document.getElementsByClassName("ck-content")[0].innerHTML });
                        }}
                        onReady={editor => {
                            console.log('Editor is ready to use!', editor);
                        }}
                    />
                </div>

                <div className="add-category-price">

                    <div className="add-category flex-col">
                        <p>Danh mục sản phẩm</p>
                        <select onInput={onChangeHandler} value={data.category} name="category">
                            {
                                categories.map((cate, index) =>
                                    <option value={cate.id} key={`cate_${index}`}>{cate.name}</option>
                                )
                            }

                        </select>
                    </div>

                    <div className="add-price flex-col">
                        <p>Giá bán sản phẩm</p>
                        <input onInput={onChangeHandler} value={data.price} type="number" name='price' placeholder='Giá bán sản phẩm' />
                    </div>

                    <div className="add-price flex-col">
                        <p>Số lượng sản phẩm</p>
                        <input onInput={onChangeHandler} value={data.quantity} type="number" name='quantity' placeholder='Số lượng sản phẩm' />
                    </div>


                </div>

                <div className="add-category-price">

                    <div className="add-category flex-col">
                        <p>Thương hiệu</p>
                        <select onInput={onChangeHandler} value={data.brand} name="brand">
                            {
                                brands.map((brand, index) =>
                                    <option value={brand.id} key={`brand_${index}`}>{brand.name}</option>
                                )
                            }
                        </select>
                    </div>

                    <div hidden>
                        <p>Số lượt xem</p>
                        <input onInput={onChangeHandler} value={data.watchCount} type="number" name='watchCount' placeholder='Số lượt xem' />
                    </div>
                </div>

                <p>Thuộc tính sản phẩm</p>
                {
                    data.category === "665eee91d176ea3961e606c0" // laptop
                    &&
                    <div className="add-category-price">
                        <div className="add-product-name flex-col">
                            <p>Card đồ họa</p>
                            <input onInput={onChangeHandler} value={data.graphic} type="text" name='graphic' placeholder='RX 5500M | GTX 1650 | RTX 3060' />
                        </div>

                        <div className="add-product-name flex-col">
                            <p>Vi xử lý</p>
                            <input onInput={onChangeHandler} value={data.cpu} type="text" name='cpu' placeholder='Ryzen 5 5600H | Intel Core i5 12500H' />
                        </div>

                        <div className="add-product-name flex-col">
                            <p>Dung lượng RAM</p>
                            <select onInput={onChangeHandler} value={data.ram} name='ram'>
                                <option value="8">8GB</option>
                                <option value="16">16GB</option>
                                <option value="32">32GB</option>
                                <option value="64">64GB</option>
                                <option value="128">128GB</option>
                            </select>

                        </div>

                        <div className="add-product-name flex-col">
                            <p>Dung lượng SSD</p>

                            <select onInput={onChangeHandler} value={data.ssd} name='ssd'>
                                <option value="128">128GB</option>
                                <option value="256">256GB</option>
                                <option value="512">512GB</option>
                                <option value="1024">1TB</option>
                                <option value="2048">2TB</option>
                                <option value="4096">4TB</option>
                            </select>
                        </div>

                        <div className="add-product-name flex-col">
                            <p>Tấm nền màn hình</p>
                            <select onInput={onChangeHandler} value={data.panel} name='panel'>
                                <option value="IPS">IPS</option>
                                <option value="OLED">OLED</option>
                                <option value="VA">VA</option>
                            </select>
                        </div>

                        <div className="add-product-name flex-col">
                            <p>Kích thước màn hình</p>
                            <select onInput={onChangeHandler} value={data.screen_size} name='screen_size'>
                                <option value="15">15 inch</option>
                                <option value="13">13 inch</option>
                                <option value="14">14 inch</option>
                                <option value="16">16 inch</option>
                                <option value="17">17 inch</option>
                            </select>
                        </div>

                        <div className="add-product-name flex-col">
                            <p>Độ sáng màn hình</p>
                            <input onInput={onChangeHandler} value={data.brightness} type="number" name='brightness' placeholder='500nit | 600nit | 700nit' />
                        </div>

                        <div className="add-product-name flex-col">
                            <p>Độ phân giải</p>
                            <select onInput={onChangeHandler} value={data.resolution} name='resolution'>
                                <option value="1920 x 1080">Full HD</option>
                                <option value="2160 x 1440">2k</option>
                                <option value="2880 x 1620">3k</option>
                                <option value="3840 x 2160">4K</option>
                            </select>
                        </div>

                        <div className="add-product-name flex-col">
                            <p>Trọng lượng</p>
                            <input onInput={onChangeHandler} value={data.weight} type="number" name='weight' placeholder='1.8 Kb | 2.3 Kg | 2.5 Kg' />
                        </div>
                    </div>
                }
                {
                    data.category === "665eee7ad176ea3961e606bf" // mobile phone
                    &&
                    <>
                        <div className="add-category-price">
                            <div className="add-product-name flex-col">
                                <p>Card đồ họa</p>
                                <input onInput={onChangeHandler} value={data.graphic} type="text" name='graphic' placeholder='RX 5500M | GTX 1650 | RTX 3060' />
                            </div>

                            <div className="add-product-name flex-col">
                                <p>Vi xử lý</p>
                                <input onInput={onChangeHandler} value={data.cpu} type="text" name='cpu' placeholder='Ryzen 5 5600H | Intel Core i5 12500H' />
                            </div>

                            <div className="add-product-name flex-col">
                                <p>Dung lượng RAM</p>
                                <select onInput={onChangeHandler} value={data.ram} name='ram'>
                                    <option value="8">8GB</option>
                                    <option value="16">16GB</option>
                                    <option value="32">32GB</option>
                                    <option value="64">64GB</option>
                                    <option value="128">128GB</option>
                                </select>

                            </div>

                            <div className="add-product-name flex-col">
                                <p>Dung lượng SSD</p>

                                <select onInput={onChangeHandler} value={data.ssd} name='ssd'>
                                    <option value="128">128GB</option>
                                    <option value="256">256GB</option>
                                    <option value="512">512GB</option>
                                    <option value="1024">1TB</option>
                                    <option value="2048">2TB</option>
                                    <option value="4096">4TB</option>
                                </select>
                            </div>

                            <div className="add-product-name flex-col">
                                <p>Tấm nền màn hình</p>
                                <select onInput={onChangeHandler} value={data.panel} name='panel'>
                                    <option value="IPS">IPS</option>
                                    <option value="OLED">OLED</option>
                                    <option value="VA">VA</option>
                                </select>
                            </div>

                            <div className="add-product-name flex-col">
                                <p>Kích thước màn hình</p>
                                <select onInput={onChangeHandler} value={data.screen_size} name='screen_size'>
                                    <option value="15">15 inch</option>
                                    <option value="13">13 inch</option>
                                    <option value="14">14 inch</option>
                                    <option value="16">16 inch</option>
                                    <option value="17">17 inch</option>
                                </select>
                            </div>

                            <div className="add-product-name flex-col">
                                <p>Độ sáng màn hình</p>
                                <input onInput={onChangeHandler} value={data.brightness} type="number" name='brightness' placeholder='500nit | 600nit | 700nit' />
                            </div>

                            <div className="add-product-name flex-col">
                                <p>Độ phân giải</p>
                                <select onInput={onChangeHandler} value={data.resolution} name='resolution'>
                                    <option value="1920 x 1080">Full HD</option>
                                    <option value="2160 x 1440">2k</option>
                                    <option value="2880 x 1620">3k</option>
                                    <option value="3840 x 2160">4K</option>
                                </select>
                            </div>

                            <div className="add-product-name flex-col">
                                <p>Trọng lượng</p>
                                <input onInput={onChangeHandler} value={data.weight} type="number" name='weight' placeholder='1.8 Kb | 2.3 Kg | 2.5 Kg' />
                            </div>
                        </div>
                    </>
                }

                {
                    data.category === "66615ffbc875cc7d60827534" // watch
                    &&
                    <>
                        Watch
                    </>
                }

                <button className='add-btn' type='submit'>Thêm sản phẩm</button>
            </form>
        </div>
    )
}

export default AddProduct