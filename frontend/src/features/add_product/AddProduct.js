import React, { useState } from 'react';
import styled from 'styled-components';
import Header from '../../components/Header';
import SubHeader from '../../components/SubHeader';
import {useNavigate} from 'react-router-dom';
import AddProductApi from '../../services/AddProductApi';



const Container = styled.div`
  max-width: 800px;
  margin: 24px auto;
  padding: 2rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 1px sold #000; 
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

const FormRow = styled.div`
  display: flex;
  justify-content: space-between;
  gap: 1rem;
`;

const FormGroup = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

const Label = styled.label`
  font-weight: bold;
  margin-bottom: 0.5rem;
`;

const Input = styled.input`
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const Textarea = styled.textarea`
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: vertical;
`;

const Select = styled.select`
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

const FormActions = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
`;

const Button = styled.button`
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  ${({ cancel }) => cancel ? `
    background-color: white;
    color: black;
    border: 1px solid #ccc;
  ` : `
    background-color: black;
    color: white;
  `}
`;

const AddProduct = () => {
  const navigate = useNavigate();
  const [product, setProduct] = useState({
    title: '',
    description: '',
    price: '',
    categoryId: '',
    productCondition: '',
    useDuration: '',
    shippingType: '',
    quantityAvailable: '',
  });
  
  const categories = [
    { categoryId: 1, name: 'Electronics' },
    { categoryId: 2, name: 'Books' },
    { categoryId: 3, name: 'Clothing' }
  ];

  const headerConfig = {
    search: false,
    requests: true,
    notifications: true,
    add: true,
    profile: true
  };

  const [files, setFiles] = useState([]);

  const handleChange = (e) => {
    setProduct({
      ...product,
      [e.target.name]: e.target.value,
    });
  };

  const handleFileChange = (e) => {
    setFiles(Array.from(e.target.files));
  };
  

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    files.forEach((file, index) => {
      formData.append(`files`, file);
    });
    formData.append('addProductDTO', new Blob([JSON.stringify(product)], {
      type: 'application/json'
    }));

    try {
      const response = await AddProductApi.add(formData);
      console.log('Product added successfully:', response.data);
      const productId = response.data.productId;
      navigate(`/products/${productId}`);

    } catch (error) {
      console.error('There was an error adding the product!', error);
    }
  };

  return (
    <>
      <Header config={headerConfig}></Header>
      <SubHeader title={"Add New Product"} backPath={"/products"} ></SubHeader>
      <Container>
        <Form onSubmit={handleSubmit}>
          <FormRow>
            <FormGroup>
              <Label htmlFor="title">Product Name</Label>
              <Input
                type="text"
                id="title"
                name="title"
                value={product.title}
                onChange={handleChange}
                placeholder="Enter product name"
                required
              />
            </FormGroup>
            <FormGroup>
              <Label htmlFor="categoryId">Category</Label>
              <Select
                id="categoryId"
                name="categoryId"
                value={product.categoryId}
                onChange={handleChange}
                required
              >
                <option value="">Select category</option>
                {categories.map((cat) => (
                  <option key={cat.categoryId} value={cat.categoryId}>
                    {cat.name}
                  </option>
                ))}
              </Select>
            </FormGroup>
          </FormRow>
          <FormRow>
            <FormGroup>
              <Label htmlFor="description">Description</Label>
              <Textarea
                id="description"
                name="description"
                value={product.description}
                onChange={handleChange}
                placeholder="Enter product description"
                required
              />
            </FormGroup>
            <FormGroup>
              <Label htmlFor="price">Price</Label>
              <Input
                type="number"
                id="price"
                name="price"
                value={product.price}
                onChange={handleChange}
                placeholder="Enter price"
                required
              />
            </FormGroup>
          </FormRow>
          <FormRow>
            <FormGroup>
              <Label htmlFor="productCondition">Product Condition</Label>
              <Select
                id="productCondition"
                name="productCondition"
                value={product.productCondition}
                onChange={handleChange}
                required
              >
                <option value="">Select condition</option>
                <option value="New">New</option>
                <option value="Good">Good</option>
                <option value="Fair">Fair</option>
                <option value="Used">Used</option>
                <option value="Poor">Poor</option>
              </Select>
            </FormGroup>
            <FormGroup>
              <Label htmlFor="useDuration">Use Duration</Label>
              <Input
                type="text"
                id="useDuration"
                name="useDuration"
                value={product.useDuration}
                onChange={handleChange}
                placeholder="Enter use duration"
                required
              />
            </FormGroup>
          </FormRow>
          <FormRow>
            <FormGroup>
              <Label htmlFor="shippingType">Shipping Type</Label>
              <Select
                id="shippingType"
                name="shippingType"
                value={product.shippingType}
                onChange={handleChange}
                required
              >
                <option value="">Select shipping type</option>
                <option value="Free">Free</option>
                <option value="Paid">Paid</option>
              </Select>
            </FormGroup>
            <FormGroup>
              <Label htmlFor="quantityAvailable">Quantity Available</Label>
              <Input
                type="number"
                id="quantityAvailable"
                name="quantityAvailable"
                value={product.quantityAvailable}
                onChange={handleChange}
                placeholder="Enter quantity available"
                required
              />
            </FormGroup>
          </FormRow>
          <FormGroup>
            <Label htmlFor="file">Upload Images</Label>
            <Input
              type="file"
              id="file"
              name="file"
              multiple
              onChange={handleFileChange}
            />
          </FormGroup>
          <FormActions>
            <Button type="button" onClick={() => console.log('Cancel')}>
              Cancel
            </Button>
            <Button type="submit">Save Product</Button>
          </FormActions>
        </Form>
      </Container>
    </>
  );
};

export default AddProduct;
