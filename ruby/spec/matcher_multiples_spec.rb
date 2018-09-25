describe 'Tests para matchers multiples' do
  include MatcherFactory
  it 'matchea por tipo y valor' do
    resultado = duck(:+).and(type(Fixnum), val(5)).call(5)
    expect(resultado).to be true
  end

  it 'no matchea si un matcher de la composición es falso' do
    resultado = duck(:+).and(type(Fixnum), val(51)).call(5)
    expect(resultado).to be false
  end

  it 'deberia matchear con or' do
    expect(duck(:+).or(duck(:filter)).call 5).to be true
  end

  it 'no deberia matchear con nada' do
    expect(type(Object).not.call(:lo_que_sea)).to be false
  end

  it 'debería matchear si no es string' do
    expect(type(String).not.call(25.5)).to be true
  end

  it 'debería permitir anidar matchers' do
    expect(duck(:+).or(duck(:filter).and(val(5))).call 5).to be true
  end
end