// Server-Side Request Forgery (SSRF)
fn demo() {
  let body = reqwest::blocking::get(url)?.text()?;
  }
