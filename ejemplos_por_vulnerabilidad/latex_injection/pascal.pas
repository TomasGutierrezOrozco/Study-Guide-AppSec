{ LaTeX Injection }
program Example;
begin
  Latex := '\input{' + Request.QueryFields.Values['name'] + '}';
  end.
