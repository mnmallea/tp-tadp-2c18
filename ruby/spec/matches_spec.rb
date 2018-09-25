require 'rspec'

describe 'Tests para matches' do
  x = [1, 2, 3]

  it 'deberia matchear en el primer with y devolver 3' do
    expect(matches?(x) do
      with(list([:a, val(2), duck(:+)])) {a + 2}
      with(list([1, 2, 3])) {'acá no llego'}
      otherwise {'acá no llego'}
    end).to eq(3)
  end

  it 'deberia devolver chau!' do

    x = Object.new
    x.send(:define_singleton_method, :hola) {'hola'}
    expect(matches?(x) do
      with(duck(:hola)) {'chau!'}
      with(type(Object)) {'acá no llego'}
    end).to eq 'chau!'
  end


  it 'deberia entrar por el otherwise' do
    x = 2
    expect(matches?(x) do
      with(type(String)) {a + 2}
      with(list([1, 2, 3])) {'acá no llego'}
      otherwise {'acá si llego'}
    end).to eq('acá si llego')

  end

  it 'should throw exception' do
    val = 'Soy el mapa'
    expect do
      matches? val do
        with(type(Integer)) {}
        with(type(String).and(duck(:filter))) {}
        with(list([4, 5, 6])) {}
      end
    end.to raise_error(MatchError)
  end

  it 'deberia devolver nil' do
    x = 1
    resultado = matches? x do
      with(type(Integer), val(2)) {raise 'Esto no deberia pasar'}
      otherwise {}
    end
    expect(resultado).to eq nil
  end

  it 'debe bindear variables ' do
    string = 'Hola desde'
    res = matches? string do
      with(duck(:select)) {puts "bleh"}
      with(type(String), :a_string) {"#{a_string} Peter Machine for Ruby"}
      otherwise {puts "No Deberia llegar aca"}
    end
    expect(res).to eq('Hola desde Peter Machine for Ruby')
  end

  it 'deberia bindear variables en listas' do
    arr = [1, 2, 3, 4, 5, 6]

    res = matches? arr do
      with(list([:a, :b, :c])) {a + b + c}
      otherwise {false}
    end

    expect(res).to eq 6
  end

  it 'deberia matchear en el primer with y devolver 3' do
    x = [1, 2, 3]
    resultado = matches?(x) do
      with(list([:a, val(2), duck(:+)])) {a + 2}
      with(list([1, 2, 3])) {'acá no llego'}
      otherwise {'acá no llego v2'}
    end
    expect(resultado).to eq 3
  end


end