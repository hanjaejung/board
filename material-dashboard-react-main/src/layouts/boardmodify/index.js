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
import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';

// @mui material components
import Card from "@mui/material/Card";

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDInput from 'components/MDInput';
import MDButton from "components/MDButton";

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";

// Data
import axios from 'axios';

function BoardModify() {
  const { state } = useLocation();
  console.log(state);
  const [title, setTitle] = useState(state.title);
  const [body, setBody] = useState(state.body);
  const [id, setId] = useState(state.id);
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleModifyBoard = (event) => {
    console.log(localStorage.getItem('token'));
    console.log('title : ' + title);
    console.log('body : ' + body);
    console.log('id : ' + id);

    axios({
      url: '/api/board/' + id,
      method: 'PUT',
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
        })
        .catch((error) => {
          setOpen(true);

          console.log(error);
        });
  };

  return (
    <DashboardLayout>
      <MDBox pt={6} pb={3}>
        <Card>
          <MDBox pt={4} pb={3} px={3}>
            <MDBox component="form" role="form">
              <MDBox mb={2}>
                <MDInput
                    label="Title"
                    defaultValue={title}
                    onChange={(v) => setTitle(v.target.value)}
                    fullWidth
                />
              </MDBox>
              <MDBox mb={2}>
                <MDInput
                    label="Body"
                    defaultValue={body}
                    multiline
                    rows={20}
                    onChange={(v) => setBody(v.target.value)}
                    fullWidth
                />
              </MDBox>
              <MDBox mt={4} mb={1} right>
                <MDButton onClick={handleModifyBoard} variant="gradient" color="info">
                  Update
                </MDButton>
              </MDBox>
            </MDBox>
          </MDBox>
        </Card>
      </MDBox>
    </DashboardLayout>
  );
}

export default BoardModify;
