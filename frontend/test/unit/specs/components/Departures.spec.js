import { mount } from 'vue-test-utils';
import Departures from '@/components/Departures';

describe('Departures.vue', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(Departures, {
      propsData: {
        transports: [
          {
            displaytime: 'Nu',
            linenumber: '179',
            destination: 'Vällingby',
            expecteddatetime: '2017-08-24T21:26:00',
            journeynumber: 22176,
          },
          {
            displaytime: '21:32',
            linenumber: '523',
            destination: 'Sollentuna station',
            expecteddatetime: '2017-08-24T21:32:00',
            journeynumber: 22202,
          },
        ],
      },
    });
  });

  it('should render one row per departure', () => {
    expect(wrapper.findAll('.departure__row').length).to.equal(2);
  });

  it('should render correct content', () => {
    expect(wrapper.findAll('.departure__line').at(0).text()).to.equal('179');
    expect(wrapper.findAll('.departure__line').at(1).text()).to.equal('523');
    expect(wrapper.findAll('.departure__destination').at(0).text()).to.equal('Vällingby');
    expect(wrapper.findAll('.departure__destination').at(1).text()).to.equal('Sollentuna station');
    expect(wrapper.findAll('.departure__row-right').at(0).text()).to.equal('Nu');
    expect(wrapper.findAll('.departure__row-right').at(1).text()).to.equal('21:32');
  });
});
