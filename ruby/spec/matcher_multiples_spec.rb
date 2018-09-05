require 'rspec'
require_relative '../lib/matcher'
require_relative '../lib/metamodelo'

describe 'Tests para matchers múltiples' do
  it 'matchea por tipo y valor' do
    resultado = Matcher.duck(:+).and(Matcher.type(Fixnum), Matcher.val(5)).call(5)
    expect(resultado).to be true
  end

  it 'no matchea si un matcher de la composición es falso' do
    resultado = Matcher.duck(:+).and(Matcher.type(Fixnum), Matcher.val(51)).call(5)
    expect(resultado).to be false
  end

  it 'debería matchear con or' do
    expect(Matcher.duck(:+).or(Matcher.duck(:filter)).call 5).to be true
  end

  it 'no deberia matchear con nada' do
    expect(Matcher.type(Object).not.call(:lo_que_sea)).to be false
  end

  it 'debería matchear si no es string' do
    expect(Matcher.type(String).not.call(25.5)).to be true
  end

end