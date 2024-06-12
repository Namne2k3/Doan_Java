import { BrowserRouter, Routes, Route } from "react-router-dom";
import { userService } from './services';
import ProfilePage from './pages/user/ProfilePage';
import RegistrationPage from './pages/auth/Register';
import UserManagementPage from './pages/user/UserManagementPage';
import UpdateUser from './pages/user/UpdateUser';
import Navbar from './components/Navbar/Navbar';
import Home from './pages/home/Home';
import Cart from './pages/cart/Cart';
import PlaceOrder from './pages/placeorder/PlaceOrder';
import Footer from "./components/Footer/Footer";
import { useState, useEffect, useContext } from "react";
import LoginPopup from "./components/LoginPopup/LoginPopup";
import Admin from "./pages/admin/Admin";
import AddProduct from "./pages/admin/Products/AddProduct/AddProduct";
import ListProduct from "./pages/admin/Products/ListProduct/ListProduct";
import ListOrder from "./pages/admin/Orders/ListOrder/ListOrder";
import UpdateProduct from "./pages/admin/Products/UpdateProduct/UpdateProduct";
import { StoreContext } from "./context/StoreContext";
import Success from "./components/Success/Success";

function App() {

  const [showLogin, setShowLogin] = useState(false);
  const { fetchProfileData } = useContext(StoreContext);

  useEffect(() => {
    fetchProfileData()
  }, [])

  return (
    <BrowserRouter>
      <>
        {
          showLogin
            ?
            <LoginPopup setShowLogin={setShowLogin} />
            :
            <></>
        }
        <div className="app">
          <Navbar setShowLogin={setShowLogin} />
          <Routes>
            <Route exact path="/" element={<Home />} />
            <Route exact path="/cart" element={<Cart />} />
            <Route exact path="/order" element={<PlaceOrder />} />
            <Route path="/register" element={<RegistrationPage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/success" element={<Success />} />
            {/* <Route exact path="/login" element={<LoginPage />} /> */}

            {/* {userService.adminOnly() && (
              <>
              <Route path="/admin" element={<Admin />} />
              <Route path="/admin/products" element={<ListProduct />} />
              <Route path="/admin/products/add" element={<AddProduct />} />
              <Route path="/admin/products/update" element={<UpdateProduct />} />
              <Route path="/admin/orders" element={<ListOrder />} />
              <Route path="/admin/user-management" element={<UserManagementPage />} />
              <Route path="/update-user/:userId" element={<UpdateUser />} />
              </>
              )} */}

            {/* <Route path="*" element={<Navigate to="/" />} />‰ */}
          </Routes>
          {
            userService.adminOnly()
            &&
            <Routes>
              <Route path="/admin" element={<Admin />}>
                <Route path="products" element={<ListProduct />} />
                <Route path="products/add" element={<AddProduct />} />
                <Route path="products/update" element={<UpdateProduct />} />
                <Route path="user-management" element={<UserManagementPage />} />
                <Route path="update-user/:userId" element={<UpdateUser />} />
                <Route path="orders" element={<ListOrder />} />
              </Route>
            </Routes>
          }
        </div>
        <Footer />
      </>
    </BrowserRouter>
  );
}

export default App;
