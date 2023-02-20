function loginApi(data) {
    return $axios({
      'url': '/user/user/login',
      'method': 'post',
      data
    })
  }

function loginoutApi() {
  return $axios({
    'url': '/user/user/loginout',
    'method': 'post',
  })
}

  