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
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import KeyboardArrowLeftIcon from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';

// Material Dashboard 2 React components
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import MDButton from "components/MDButton";
import MDPagination from 'components/MDPagination';
import MDInput from 'components/MDInput';

// Material Dashboard 2 React example components
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";

import axios from 'axios';
import Icon from "@mui/material/Icon";

function BoardDetail() {
  const { state } = useLocation();
  console.log(state);
  const [page, setPage] = useState(0);
  const [title, setTitle] = useState(state.title);
  const [writer, setWriter] = useState(state.user.userName);
  const [body, setBody] = useState(state.body);
  const [id, setId] = useState(state.id);

  const [likes, setLikes] = useState(0);
  const [comments, setComments] = useState([]);
  const [totalPage, setTotalPage] = useState(0);
  const [comment, setComment] = useState();

  //대댓글
    const [reply, setReply] = useState([]);
    const [replyStyle, setReplyStyle] = useState({display: 'none'});

    const [replyComments, setReplyComments] = useState([]);

    const [replyCommentPicks, setCommentPicks] = useState([]);

  const handleLikePost = (event) => {
    console.log(localStorage.getItem('token'));
    axios({
      url: '/api/board/' + id + '/likes',
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
      },
    })
        .then((res) => {
          console.log('success');
          handleLikeCounts();
        })
        .catch((error) => {
          console.log(error);
        });
  };

  const handleLikeCounts = (event) => {
    console.log(localStorage.getItem('token'));
    axios({
      url: '/api/board/' + id + '/likes',
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
      },
    })
        .then((res) => {
          console.log('success');
          setLikes(res.data.result);
        })
        .catch((error) => {
          console.log(error);
        });
  };

  const changePage = (pageNum) => {
    console.log('change pages');
    console.log(pageNum);
    console.log(page);
    setPage(pageNum);
    handleGetComments(pageNum);
  };

  const handleGetComments = (pageNum, event) => {
    console.log('handleGetComments');
    axios({
      url: '/api/board/' + id + '/comments?size=5&sort=id&page=' + pageNum,
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
      },
    })
        .then((res) => {
          console.log('success');
          console.log(res);
          setComments(res.data.result.content);
          setTotalPage(res.data.result.totalPages);
        })
        .catch((error) => {
          console.log(error);
        });
  };

  const handleWriteComment = (pageNum, event) => {
    console.log('handleWriteComment');
    axios({
      url: '/api/board/' + id + '/comments',
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
      },
      data: {
        comment: comment,
      },
    })
        .then((res) => {
          console.log('success');
          handleGetComments();
        })
        .catch((error) => {
          console.log(error);
        });
  };

  const handlerWriteReply = (commentId, event) => {
      console.log('handlerWriteReply');
      console.log(commentId);
      console.log(event);
      axios({
          url: '/api/board/' + commentId + '/reply',
          method: 'POST',
          headers: {
              Authorization: 'Bearer ' + localStorage.getItem('token'),
          },
          data: {
              reply: reply,
          },
      })
          .then((res) => {
              console.log('success');
              handleGetReplys(0,commentId);
          })
          .catch((error) => {
              console.log(error);
          });
  };

  const handleReplyStyle = (commentId, event) => {

      // if(replyStyle.display === 'block'){
      //
      //     setReplyStyle({display: 'none'});
      // }else{
      //     setReplyStyle({display: 'block'});
      //     handleGetReplys(0,commentId);
      // }

      if(replyCommentPicks.length == 0){
          handleGetReplys(0,commentId);
      }else {
          setCommentPicks('');
      }

  };

  const handleGetReplys = (pageNum, id, event) => {
      console.log('handleGetReplys');
      axios({
          url: '/api/board/' + id + '/replyComments?size=5&sort=id&page=' + pageNum,
          method: 'GET',
          headers: {
              Authorization: 'Bearer ' + localStorage.getItem('token'),
          },
      })
          .then((res) => {
              console.log('success');
              console.log(res);
              setReplyComments(res.data.result.content);
              setCommentPicks(id);
          })
          .catch((error) => {
              console.log(error);
          });
  };
  useEffect(() => {
    handleGetComments();
    handleLikeCounts();
  }, '');

  return (
    <DashboardLayout>
      <MDBox pt={6} pb={3}>
        <Card>
          <MDBox pt={4} pb={3} px={3}>
            <Grid container>
              <Grid item xs={6}>
                <MDTypography fontWeight="bold" variant="body2">
                  {title}
                </MDTypography>
              </Grid>
              <Grid item xs={6}>
                <MDTypography variant="body2" textAlign="right">
                  {writer}
                </MDTypography>
              </Grid>
            </Grid>
            <MDTypography variant="body2">{body}</MDTypography>
            <MDTypography variant="body2">{likes}
                <Icon color="inherit" fontSize="inherit">
                  favorite
                </Icon>
            </MDTypography>
          </MDBox>
        </Card>
      </MDBox>

      <MDButton onClick={handleLikePost} variant="gradient" color="info">
        LIKE1
      </MDButton>
      {comments.map((comment) => (
          <MDBox pt={4} pb={4}>
            <Card>
              <MDBox pt={2} pb={2} px={3}>
                <Grid container>
                  <Grid item xs={6}>
                    <MDTypography fontWeight="bold" variant="body2">
                      {comment.comment}
                    </MDTypography>
                  </Grid>
                  <Grid item xs={6}>
                    <MDTypography variant="body2" textAlign="right">
                      {comment.userName}
                    </MDTypography>
                  </Grid>
                </Grid>
                <MDTypography variant="body2">{comment.body}</MDTypography>
              </MDBox>
                <MDBox pt={2} pb={2} px={3} right>
                    <MDButton onClick={(e) => handleReplyStyle(comment.id,e)} variant="gradient" color="info">
                        Reply
                    </MDButton>
                </MDBox>
                {replyComments.filter(replyComment => comment.id === replyComment.commentId).map((replyComment) => (
                    <MDBox pt={2} pb={2} px={3}>
                        <Grid container style={comment.id === replyCommentPicks ? {display : "block"} : {display : "none"}}>
                            <Grid item xs={6}>
                                <MDTypography fontWeight="bold" variant="body2">
                                    {replyComment.replyComment}
                                </MDTypography>
                            </Grid>
                            <Grid item xs={6}>
                                <MDTypography variant="body2" textAlign="right">
                                    {replyComment.userName}
                                </MDTypography>
                            </Grid>
                        </Grid>
                        <MDTypography variant="body2">{comment.body}</MDTypography>
                    </MDBox>

                ))}

                <MDBox component="form" role="form" >
                    <MDBox pt={2} pb={2} px={3}>
                        <MDInput label="reply" onChange={(v) => setReply(v.target.value)} fullWidth style={comment.id === replyCommentPicks ? {display : "block"} : {display : "none"}}/>
                    </MDBox>
                    <MDBox pt={2} pb={2} px={3} right>
                        <MDButton onClick={(e) => handlerWriteReply(comment.id,e)} variant="gradient" color="info" style={comment.id === replyCommentPicks ? {display : "block"} : {display : "none"}} >
                            ReplyWrite
                        </MDButton>
                    </MDBox>
                </MDBox>




            </Card>
          </MDBox>
      ))}
      <MDPagination>
        <MDPagination item>
          <KeyboardArrowLeftIcon></KeyboardArrowLeftIcon>
        </MDPagination>
        {[...Array(totalPage).keys()].map((i) => (
            <MDPagination item onClick={() => changePage(i)}>
              {i + 1}
            </MDPagination>
        ))}
        <MDPagination item>
          <KeyboardArrowRightIcon></KeyboardArrowRightIcon>
        </MDPagination>
      </MDPagination>


      <MDBox pt={3} pb={3}>
        <Card>
          <MDBox component="form" role="form">
            <MDBox pt={2} pb={2} px={3}>
              <MDInput label="comment" onChange={(v) => setComment(v.target.value)} fullWidth />
            </MDBox>
            <MDBox pt={2} pb={2} px={3} right>
              <MDButton onClick={handleWriteComment} variant="gradient" color="info">
                Comment
              </MDButton>
            </MDBox>
          </MDBox>
        </Card>
      </MDBox>
    </DashboardLayout>
  );
}

export default BoardDetail;
