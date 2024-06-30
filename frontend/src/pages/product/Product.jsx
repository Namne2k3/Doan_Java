/* eslint-disable jsx-a11y/alt-text */
/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useContext, useEffect, useState } from 'react'
import CheckoutListButton from '../../components/CheckoutListButton/CheckoutListButton';
import "./Product.css"
import { useParams } from 'react-router-dom'
import axios from 'axios';
import { StoreContext } from '../../context/StoreContext';
const Product = () => {

    const params = useParams();
    const { productId } = params;

    const [data, setData] = useState({});
    const [popular, setPopular] = useState([])

    const { addToCart } = useContext(StoreContext);

    const [cart, setCart] = useState({
        id: "",
        user: "",
        product: {},
        createdAt: "",
        quantity: 1
    })

    const getProductData = async () => {
        const res = await axios.get(`http://localhost:8080/api/v1/products/${productId}`)
        if (res.data) {
            setData(res.data.data)
            setCart(prev => ({ ...prev, product: res.data.data }));
            getPopularProducts(res.data.data.category.name)
        }
    }

    const getPopularProducts = async (category) => {
        const res = await axios.get(`http://localhost:8080/api/v1/products/populars?category=${category}`)
        if (res.data) {
            console.log(res.data.dataList);
            setPopular(res.data.dataList)
        }
    }

    const VNDONG = (number) => {
        return number.toLocaleString('it-IT', { style: 'currency', currency: 'VND' })
    }

    const handleChangeQuantity = (number) => {
        const value = parseInt(number, 10);

        if (value >= 1 && value <= 50) {
            setCart(prev => ({ ...prev, quantity: value }));
        }
    }

    useEffect(() => {
        console.log(data);
        getProductData()
    }, [])

    return (
        data.id &&
        <>
            <section className="py-5">
                <div className="container">
                    <div className="row gx-5">
                        <aside className="col-lg-6">
                            <div className="border rounded-4 mb-3 d-flex justify-content-center">
                                <a data-fslightbox="mygalley" className="rounded-4" target="_blank" data-type="image" href={`${data.image}`} rel="noreferrer">
                                    <img alt='product_image' style={{ maxWidth: '100%', maxHeight: '100vh', margin: 'auto' }}
                                        className="rounded-4 fit" src={`/images/${data.image}`} />
                                </a>
                            </div>
                            <div className="d-flex justify-content-center mb-3 flex-wrap gap-4">
                                {
                                    data.images.map((image, index) => (
                                        <a key={index} data-fslightbox="mygalley" className="border mx-1 rounded-2 item-thumb" target="_blank" data-type="image" href={`/images/${image}`} rel="noreferrer">
                                            <img alt='product_image' height="100" className="rounded-2" src={`/images/${image}`} />
                                        </a>
                                    ))
                                }
                            </div>
                        </aside>
                        <main className="col-lg-6">
                            <div className="ps-lg-3">
                                <h4 className="title text-dark">
                                    {
                                        data.name
                                    }
                                </h4>
                                <div className="d-flex flex-row my-3">
                                    <span className="text-success">
                                        {
                                            data.stock_quantity > 0 ?
                                                'Còn hàng'
                                                :
                                                'Hết hàng'
                                        }
                                    </span>
                                </div>

                                <div className="mb-3">
                                    <span className="h5">
                                        {
                                            VNDONG(data.price)
                                        }
                                    </span>
                                </div>

                                <p>
                                    {data.description}
                                </p>

                                <div className="row">
                                    <dt className="col-3">Thương hiệu</dt>
                                    <dd className="col-9">{data.brand.name}</dd>
                                </div>

                                <hr />

                                <div className="row mb-4">
                                    <div className="col-md-4 col-6 mb-3">
                                        <label className="mb-2 d-block">Số lượng</label>
                                        <div className="input-group mb-3">
                                            <input value={cart.quantity} onChange={(e) => handleChangeQuantity(e.target.value)} min={1} max={50} type="number" className="form-control text-center border border-secondary" placeholder="1" aria-label="Example text with button addon" aria-describedby="button-addon1" />
                                        </div>
                                    </div>
                                </div>
                                <div className="d-flex gap-2">
                                    <CheckoutListButton carts={[cart]} text='Mua ngay' />
                                    <button onClick={() => addToCart(productId)} href="#" className="btn btn-success shadow-0"> <i className="me-1 fa fa-shopping-basket"></i> Thêm giỏ hàng </button>
                                    {/* <a href="#" className="btn btn-light border border-secondary py-2 icon-hover px-3"> <i className="me-1 fa fa-heart fa-lg"></i> Lưu </a> */}
                                </div>
                            </div>
                        </main>
                    </div>
                </div>
            </section>
            <section className="bg-light border-top py-4">
                <div className="container">
                    <div className="row gx-4">
                        <div className="col-lg-8 mb-4">
                            <div className="border rounded-2 px-3 py-2 bg-white">

                                <div className="tab-content" id="ex1-content">
                                    <div className="tab-pane fade show active" id="ex1-pills-1" role="tabpanel" aria-labelledby="ex1-tab-1">
                                        {
                                            data.category.name === "Laptop" &&
                                            <table className="table border mt-3 mb-2">
                                                <tr>
                                                    <th className="py-2 px-2">Display:</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.screen_size + `-inch ` + data.product_attributes.resolution + ` ` + data.product_attributes.panel
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Processor capacity:</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.cpu
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">SSD:</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.ssd + `GB`
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Memory</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.ram + `GB`
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Graphics</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.graphic
                                                        }
                                                    </td>
                                                </tr>
                                            </table>
                                        }
                                        {
                                            data.category.name === "Mobile Phone" &&
                                            <table className="table border mt-3 mb-2">
                                                <tr>
                                                    <th className="py-2 px-2">Display</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.screen_size + `-inch ` + data.product_attributes.resolution
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Công nghệ màn hình</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.screen_tech
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Processor capacity</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.cpu
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">SSD</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.ssd + `GB`
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Memory</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.ram + `GB`
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Chipset</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.chipset
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Pin</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.battery
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Camera sau</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.back_camera
                                                        }
                                                        < br />
                                                        {
                                                            data.product_attributes.video_feature_back
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Camera trước</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.front_camera
                                                        }
                                                        <br />
                                                        {
                                                            data.product_attributes.video_feature_front
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Khả năng quay video</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.video_record
                                                        }
                                                    </td>
                                                </tr>
                                            </table>
                                        }
                                        {
                                            data.category.name === "Watch" &&
                                            <table className="table border mt-3 mb-2">
                                                <tr>
                                                    <th className="py-2 px-2">Độ phân giải</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.screen_size + `-inch ` + data.product_attributes.resolution
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Công nghệ màn hình:</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.screen_tech
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Đường kính:</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.diameter + ' cm'
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Thời gian sạc</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.time_charge + ' giờ'
                                                        }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th className="py-2 px-2">Thời lượng sử dụng</th>
                                                    <td className="py-2 px-2">
                                                        {
                                                            data.product_attributes.battery_life + ' giờ'
                                                        }
                                                    </td>
                                                </tr>
                                            </table>
                                        }
                                        <div className='py-4' dangerouslySetInnerHTML={{ __html: data.content }}></div>

                                    </div>
                                </div>

                            </div>
                        </div>
                        <div className="col-lg-4">
                            <div className="px-0 border rounded-2 shadow-0">
                                <div className="card">
                                    <div className="card-body">
                                        <h5 className="card-title">Sản phẩm nổi bật</h5>
                                        {
                                            popular.map((item, index) => {

                                                return (
                                                    <div key={index} className="d-flex mb-3">
                                                        <a href="#" className="me-3">
                                                            <img src={`/images/${item.image}`} style={{ minWidth: '96px', height: '96px' }} className="img-md img-thumbnail" />
                                                        </a>
                                                        <div className="info">
                                                            <a href="#" className="nav-link mb-1">
                                                                Rucksack Backpack Large <br />
                                                                Line Mounts
                                                            </a>
                                                            <strong className="text-dark"> $38.90</strong>
                                                        </div>
                                                    </div>
                                                )
                                            })
                                        }
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    )
}

export default Product