/**
=========================================================
* Material Dashboard 2 React - v2.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* Copyright 2022 Creative Tim (https://www.creative-tim.com)

Coded by www.creative-tim.com

 =========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/

import * as React from 'react';
import { useState } from 'react';

// @mui material components
import Card from '@mui/material/Card';

// Material Dashboard 2 React components
import MDBox from 'components/MDBox';
import MDInput from 'components/MDInput';
import MDButton from 'components/MDButton';

// Material Dashboard 2 React example components
import DashboardLayout from 'examples/LayoutContainers/DashboardLayout';

import axios from 'axios';
import {useNavigate} from "react-router";

function Table() {
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');
  const [posts, setPosts] = useState([]);
  const [totalPage, setTotalPage] = useState(0);
  const [open, setOpen] = React.useState(false);
  const navigate = useNavigate();

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleWriteBoard = (event) => {
    console.log(localStorage.getItem('token'));
    console.log('title : ' + title);
    console.log('body : ' + body);

    axios({
      url: '/api/board',
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
      },
      data: {
        title: title,
        body: body,
      },
    })
        .then((res) => {
          setOpen(true);
          console.log('success');
          handleGetTables(0);
        })
        .catch((error) => {
          setOpen(true);
          console.log(error);
          navigate('/authentication/sign-in');
        });
  };

  const handleGetTables = (pageNum, event) => {
    console.log('handleGetTables');
    axios({
      url: '/api/board?size=5&sort=id&page=' + pageNum,
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
      },
    })
        .then((res) => {
          console.log('success');
          console.log(res);
          setPosts(res.data.result.content);
          setTotalPage(res.data.result.totalPages);
          navigate('/tables');
        })
        .catch((error) => {
          console.log(error);
          navigate('/authentication/sign-in');
        });
  };

  return (
      <DashboardLayout>
        <MDBox pt={6} pb={3}>
          <Card>
            <MDBox pt={4} pb={3} px={3}>
              <MDBox component="form" role="form">
                <MDBox mb={2}>
                  <MDInput label="Title" onChange={(v) => setTitle(v.target.value)} fullWidth />
                </MDBox>
                <MDBox mb={2}>
                  <MDInput
                      label="Body"
                      multiline
                      rows={20}
                      onChange={(v) => setBody(v.target.value)}
                      fullWidth
                  />
                </MDBox>
                <MDBox mt={4} mb={1} right>
                  <MDButton onClick={handleWriteBoard} variant="gradient" color="info">
                    Save
                  </MDButton>
                </MDBox>
              </MDBox>
            </MDBox>
          </Card>
        </MDBox>
      </DashboardLayout>
  );
}

export default Table;
