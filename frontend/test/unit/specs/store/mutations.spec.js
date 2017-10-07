import { mutations } from '@/store';

const { newDepartures, addStop, removeStop } = mutations;

describe('Mutations', () => {
  describe('newDepartures', () => {
    it('should add departures to selected stops', () => {
      const state = {
        selectedItems: [
          {
            stop: { name: 'Brogatan', siteid: 3470 },
            type: 'Buses',
            timeWindow: 20,
          },
          {
            stop: { name: 'Hallonbergen', siteid: 9303 },
            type: 'Buses',
            timeWindow: 20,
          },
          {
            stop: { name: 'Kista', siteid: 9302 },
            type: 'Buses',
            timeWindow: 20,
          },
        ],
      };
      newDepartures(state, {
        siteId: 3470,
        type: 'Buses',
        data: {
          latestupdate: '2017-08-25T16:52:04',
          buses: [
            {
              displaytime: 'Nu',
              linenumber: '505',
              destination: 'Solna centrum',
              expecteddatetime: '2017-08-25T16:52:22',
              journeynumber: 21639,
            },
            {
              displaytime: '8 min',
              linenumber: '540',
              destination: 'Tensta centrum',
              expecteddatetime: '2017-08-25T17:01:00',
              journeynumber: 21602,
            },
          ],
        },
      });
      expect(state.selectedItems[0].departures.length).to.equal(2);
      expect(state.selectedItems[1].departures).to.be.undefined;
      expect(state.selectedItems[2].departures).to.be.undefined;
      expect(state.selectedItems[0].latestUpdate).to.equal('2017-08-25T16:52:04');
    });

    it('should only add new departures if the transport type match', () => {
      const state = {
        selectedItems: [
          {
            stop: { name: 'Brogatan', siteid: 3470 },
            type: 'Buses',
            timeWindow: 20,
          },
          {
            stop: { name: 'Hallonbergen', siteid: 9303 },
            type: 'Metros',
            timeWindow: 20,
          },
          {
            stop: { name: 'Hallonbergen', siteid: 9303 },
            type: 'Buses',
            timeWindow: 20,
          },
        ],
      };
      newDepartures(state, {
        siteId: 9303,
        type: 'Buses',
        data: {
          latestupdate: '2017-08-25T16:52:04',
          buses: [
            {
              displaytime: 'Nu',
              linenumber: '505',
              destination: 'Solna centrum',
              expecteddatetime: '2017-08-25T16:52:22',
              journeynumber: 21639,
            },
            {
              displaytime: '8 min',
              linenumber: '540',
              destination: 'Tensta centrum',
              expecteddatetime: '2017-08-25T17:01:00',
              journeynumber: 21602,
            },
          ],
        },
      });
      expect(state.selectedItems[0].departures).to.be.undefined;
      expect(state.selectedItems[1].departures).to.be.undefined;
      expect(state.selectedItems[2].departures.length).to.equal(2);
    });
  });

  describe('addStop', () => {
    it('should add a new stop', () => {
      const state = { selectedItems: [] };

      expect(state.selectedItems.length).to.equal(0);
      addStop(state, {
        item: {
          stop: {
            name: 'foo',
            siteid: '42',
          },
          type: 'Buses',
          timewindow: 20,
        },
      });
      expect(state.selectedItems.length).to.equal(1);
    });

    it('should update an existing stop', () => {
      const state = {
        selectedItems: [
          {
            stop: { name: 'Brogatan', siteid: 3470 },
            type: 'Buses',
            timeWindow: 20,
          },
          {
            stop: { name: 'Hallonbergen', siteid: 9303 },
            type: 'Buses',
            timeWindow: 20,
          },
          {
            stop: { name: 'Kista', siteid: 9302 },
            type: 'Buses',
            timeWindow: 20,
          },
        ],
      };

      expect(state.selectedItems.length).to.equal(3);
      addStop(state, {
        item: {
          stop: {
            name: 'foo',
            siteid: 42,
          },
          type: 'Metros',
          timewindow: 20,
        },
      });
      expect(state.selectedItems.length).to.equal(4);
      expect(state.selectedItems[3].stop.name).to.equal('foo');
    });
  });

  describe('removeStop', () => {
    it('should remove stop', () => {
      const state = {
        selectedItems: [
          {
            _id: '59cea85a0bc26d5d83037a77',
            stop: { name: 'Brogatan', siteid: 3470 },
            type: 'Buses',
            timeWindow: 20,
            latestUpdate: '2017-08-24T21:26:27',
            departures: [
              {
                displaytime: '21:36',
                linenumber: '505',
                destination: 'Solna centrum',
                expecteddatetime: '2017-08-24T21:36:01',
                journeynumber: 22203,
              },
            ],
          },
          {
            _id: '59d107963b41e204db9e8d12',
            stop: { name: 'Hallonbergen', siteid: 9303 },
            type: 'Buses',
            timeWindow: 20,
            departures: [
              {
                displaytime: '2 min',
                linenumber: '118',
                destination: 'Vällingby',
                expecteddatetime: '2017-08-24T21:29:00',
                journeynumber: 41187,
              },
              {
                displaytime: '21:34',
                linenumber: '504',
                destination: 'Rissne',
                expecteddatetime: '2017-08-24T21:34:00',
                journeynumber: 22188,
              },
            ],
            latestUpdate: '2017-08-24T21:26:27',
          },
          {
            _id: '56d107933c41e204db9e8d14',
            stop: { name: 'Kista', siteid: 9302 },
            type: 'Buses',
            timeWindow: 20,
            departures: [
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
            latestUpdate: '2017-08-24T21:25:57',
          },
        ],
      };

      expect(state.selectedItems.length).to.equal(3);
      removeStop(state, { id: '59d107963b41e204db9e8d12' });
      expect(state.selectedItems.length).to.equal(2);
      expect(state.selectedItems.findIndex(item => item._id === '59d107963b41e204db9e8d12')).to.equal(-1);
    });
  });
});
