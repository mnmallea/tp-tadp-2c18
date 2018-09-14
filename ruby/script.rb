require_relative 'lib/metamodelo'

x = 1
matches? x do
  with(val(1), type(Integer)){puts x}
  otherwise{puts "hola"}
end