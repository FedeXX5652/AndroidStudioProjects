var json = {};

function incLevel() {

}

function setRunes() {

}

function show() {

}

function show() {
    // set cytoscape to div
    var cy = cytoscape({
                          container: document.getElementById('cy'),
                          elements: [
                            { data: { id: 'a' } },
                            { data: { id: 'b' } },
                            { data: { id: 'c' } },
                            { data: { id: 'd' } },
                            { data: { id: 'e' } },
                            { data: { id: 'f' } },
                            {
                              data: {
                                id: 'ab',
                                source: 'a',
                                target: 'b'
                              }
                            },
                            {
                              data: {
                                id: 'ac',
                                source: 'a',
                                target: 'c'
                              }
                            }],
                          layout: {
                              name: 'random'
                          }
                        });
}

function getGraph() {
    console.log("getGraph");
    var cy = cytoscape({
                container: document.getElementById('cy'),
                elements: [
                  { data: { id: 'a' } },
                  { data: { id: 'b' } },
                  { data: { id: 'c' } },
                  {
                    data: {
                      id: 'ab',
                      source: 'a',
                      target: 'b'
                    }
                  },
                  {
                    data: {
                      id: 'ac',
                      source: 'a',
                      target: 'c'
                    }
                  }],
                layout: {
                    name: 'random'
                }
              });
    console.log(cy.nodes().length);
    return cy;
}
