require_relative 'lib/matcher'
require_relative 'lib/metamodelo'
require_relative 'lib/pattern'


x = 1
matches? x do
  with(type(String)) {puts "Soy un String"}
  with(val(1), type(Integer)) {puts x}
  otherwise {puts "hola"}
end