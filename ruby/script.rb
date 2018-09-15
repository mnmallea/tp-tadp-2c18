require_relative 'lib/metamodelo'

x = 1
matches?(x) do
  with(val(1), type(Integer)) { puts x }
  otherwise { puts "hola" }
end

=begin
y = "holita"
matches?(y) do
  with(:a_string) { p a_string.length }
  otherwise {puts "commitea capo"}
end

z = [4, 8]

matches?(z) do
  with(:k, :m) {p k + m}
  otherwise {puts "commitea capo2"}
end
=end