require_relative 'lib/ib/matcher'
require_relative 'lib/metamodelo'
require_relative 'lib/pattern'


x = 1
matches? x do
  with(type(String).or(duck(:+))) {puts "Soy un String"}
  with(val(1), type(Integer)) {puts self}
  otherwise {puts "hola"}
end

string = 'Saludos desde'
matches? string do
  with(duck(:select)) {puts "bleh"}
  with(type(String), :a_string) {puts "#{a_string} Peter Machine for Ruby"}
  otherwise {puts "No Deberia llegar aca"}
end

p (matches? x do
  with(duck(:+, :*), :numero) do
    numero + 1
  end
end)

arr = [1, 2, 3, 4, 5, 6]

matches? arr do
  with(list([1, 2, 3], false)) do
    puts 'divine'
  end
end

matches? arr do
  with(list([:a, :b, :c], false)) do
    puts a + b + c
  end
end

matches? arr do
  with(list([type(String)]))do
    'acá no debería entrar'
  end
  with(list([:a, :b, :c], true)) do
    puts (a + b + c)
  end
  otherwise {p 'Deberia entrar aca'}
end